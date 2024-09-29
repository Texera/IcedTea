package edu.uci.ics.amber.engine.architecture.worker

import com.google.protobuf.timestamp.Timestamp
import edu.uci.ics.amber.engine.architecture.controller.promisehandlers.ConsoleMessageHandler.ConsoleMessageTriggered
import edu.uci.ics.amber.engine.architecture.worker.WorkflowWorker.{DPInputQueueElement, MainThreadDelegateMessage}
import edu.uci.ics.amber.engine.architecture.logreplay.ReplayLogManager
import edu.uci.ics.amber.engine.architecture.worker.controlcommands.{ConsoleMessage, ConsoleMessageType}
import edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerState.{READY, UNINITIALIZED}
import edu.uci.ics.amber.engine.common.AmberLogging
import edu.uci.ics.amber.engine.common.actormessage.{ActorCommand, Backpressure}
import edu.uci.ics.amber.engine.common.ambermessage.{ChannelMarkerPayload, ControlPayload, DataPayload, WorkflowFIFOMessage}
import edu.uci.ics.amber.engine.common.virtualidentity.{ActorVirtualIdentity, ChannelIdentity}
import edu.uci.ics.amber.engine.common.virtualidentity.util.{CONTROLLER, SELF}
import edu.uci.ics.amber.error.ErrorUtils.{mkConsoleMessage, safely}

import java.time.Instant
import java.util.concurrent.{CompletableFuture, ExecutorService, Executors, Future, LinkedBlockingQueue}

class DPThread(
    val actorId: ActorVirtualIdentity,
    dp: DataProcessor,
    logManager: ReplayLogManager,
    internalQueue: LinkedBlockingQueue[DPInputQueueElement]
) extends AmberLogging {

  // initialize dp thread upon construction
  @transient
  var dpThreadExecutor: ExecutorService = _
  @transient
  var dpThread: Future[_] = _

  var backpressureStatus = false

  def getThreadName: String = "DP-thread"

  private val endFuture = new CompletableFuture[Unit]()

  def stop(): Unit = {
    if (dpThread != null) {
      dpThread.cancel(true) // interrupt
      stopped = true
      endFuture.get()
    }
    if (dpThreadExecutor != null) {
      dpThreadExecutor.shutdownNow() // destroy thread
    }
  }

  @volatile
  private var stopped = false

  def start(): Unit = {
    if (dpThreadExecutor != null) {
      logger.info("DP Thread is already running")
      return
    }
    dpThreadExecutor = Executors.newSingleThreadExecutor
    if (dp.stateManager.getCurrentState == UNINITIALIZED) {
      dp.stateManager.transitTo(READY)
    }
    if (dpThread == null) {
      // TODO: setup context
      // operator.context = new OperatorContext(new TimeService(logManager))
      val startFuture = new CompletableFuture[Unit]()
      dpThread = dpThreadExecutor.submit(new Runnable() {
        def run(): Unit = {
          Thread.currentThread().setName(getThreadName)
          logger.info("DP thread started")
          startFuture.complete(())
          dp.statisticsManager.initializeWorkerStartTime(System.nanoTime())
          try {
            runDPThreadMainLogic()
          } catch safely {
            case _: InterruptedException =>
              // dp thread will stop here
              logger.info("DP Thread exits")
            case err: Throwable =>
              logger.error("DP Thread exists unexpectedly", err)
              dp.outputHandler(Left(MainThreadDelegateMessage((worker) => {
                // notify main thread
                throw err
              })))
          }
          dp.statisticsManager.updateTotalExecutionTime(System.nanoTime())
          endFuture.complete(())
        }
      })
      startFuture.get()
    }
  }

  def handleActorCommand(cmd: ActorCommand): Unit = {
    cmd match {
      case Backpressure(enabled) =>
        backpressureStatus = enabled
      case _ => // no op
    }
  }


  @throws[Exception]
  private[this] def runDPThreadMainLogic(): Unit = {
    //
    // Main loop step 1: receive messages from actor and apply FIFO
    //
    var waitingForInput = false
    while (!stopped) {
      while (internalQueue.size > 0 || waitingForInput) {
        val elem = internalQueue.take
        waitingForInput = false
        elem match {
          case WorkflowWorker.FIFOMessageElement(msg) =>
            val channel = dp.inputGateway.getChannel(msg.channelId)
            channel.acceptMessage(msg)
            logger.info(s"queued $msg")
          case WorkflowWorker.TimerBasedControlElement(control) =>
            // establish order according to receiving order.
            // Note: this will not guarantee fifo & exactly-once
            // Please make sure the control here is IDEMPOTENT and ORDER-INDEPENDENT.
            val controlChannelId = ChannelIdentity(SELF, SELF, isControl = true)
            val channel = dp.inputGateway.getChannel(controlChannelId)
            channel.acceptMessage(
              WorkflowFIFOMessage(controlChannelId, channel.getCurrentSeq, control)
            )
          case WorkflowWorker.ActorCommandElement(msg) =>
            handleActorCommand(msg)
        }
      }

      //
      // Main loop step 2: do input selection
      //
      var channelId: ChannelIdentity = null
      var msgOpt: Option[WorkflowFIFOMessage] = None
      var disableFT = false
      if (
        dp.inputManager.hasUnfinishedInput || dp.outputManager.hasUnfinishedOutput || dp.pauseManager.isPaused
      ) {
        val (channelOpt, ft) =
          if(!dp.pauseManager.debuggingMode){
            dp.inputGateway.tryPickControlChannel
          }else{
            dp.inputGateway.tryPickChannel
          }
        disableFT = ft
        channelOpt match {
          case Some(channel) =>
            logger.info("picked $channelId")
            channelId = channel.channelId
            msgOpt = Some(channel.take)
          case None =>
            // continue processing
            if (!dp.pauseManager.isPaused && !backpressureStatus) {
              logger.info("continue processing")
              channelId = dp.inputManager.currentChannelId
            } else {
              logger.info("start waiting")
              waitingForInput = true
            }
        }
      } else {
        // take from input port
        val (channelOpt, ft) = if (backpressureStatus) {
          dp.inputGateway.tryPickControlChannel
        } else {
          dp.inputGateway.tryPickChannel
        }
        disableFT = ft
        channelOpt match {
          case Some(channel) =>
            channelId = channel.channelId
            msgOpt = Some(channel.take)
          case None => waitingForInput = true
        }
      }

      //
      // Main loop step 3: process selected message payload
      //
      if (channelId != null) {
        // for logging, skip large data frames.
        val msgToLog = msgOpt.filter(_.payload.isInstanceOf[ControlPayload])
        logManager.withFaultTolerant(channelId, msgToLog, disableFT) {
          msgOpt match {
            case None =>
              dp.continueDataProcessing(logManager)
            case Some(msg) =>
              msg.payload match {
                case payload: ControlPayload =>
                  dp.processControlPayload(msg.channelId, payload)
                case payload: DataPayload =>
                  dp.processDataPayload(msg.channelId, payload)
                case payload: ChannelMarkerPayload =>
                  dp.processChannelMarker(msg.channelId, payload, logManager)
              }
          }
        }
      }
      // As the computation is chopped into steps, the checkpoint
      // serialization must happen after/before a step. Otherwise
      // DP state will be restored in the middle of a step, which
      // is often not what we want. Thus, we have this one-time
      // additional serializationCall assigned inside the checkpoint
      // handler.
      dp.serializationManager.applySerialization()

      dp.statisticsManager.updateTotalExecutionTime(System.nanoTime())
      // End of Main loop
    }
  }
}

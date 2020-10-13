package web.model.event

import engine.common.ambermessage.PrincipalMessage.ReportCurrentProcessingTuple

object OperatorCurrentTuplesUpdateEvent {
  def apply(report: ReportCurrentProcessingTuple): OperatorCurrentTuplesUpdateEvent = {
    println(report)
    val workerTuples = report.tuple
      .map(p => {
        val workerName = p._2.name;
        if (p._1 == null) {
          WorkerTuples(workerName, List.empty)
        } else {
          WorkerTuples(workerName, p._1.toArray().map(v => v.toString).toList)
        }
      })
      .filter(tuples => tuples.tuple != null && tuples.tuple.nonEmpty)
      .toList
    OperatorCurrentTuplesUpdateEvent(report.operatorID, workerTuples)
  }
}

case class WorkerTuples(workerID: String, tuple: List[String])

case class OperatorCurrentTuplesUpdateEvent(operatorID: String, tuples: List[WorkerTuples])
    extends TexeraWsEvent

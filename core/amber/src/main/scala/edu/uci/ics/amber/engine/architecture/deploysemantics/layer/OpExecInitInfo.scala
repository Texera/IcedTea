package edu.uci.ics.amber.engine.architecture.deploysemantics.layer

import edu.uci.ics.texera.workflow.common.operators.OperatorExecutor
import edu.uci.ics.texera.workflow.operators.udf.java.JavaRuntimeCompilation
import edu.uci.ics.texera.workflow.operators.udf.scala.ScalaRuntimeCompilation

object OpExecInitInfo {

  type OpExecFunc = (Int, Int) => OperatorExecutor
  type JavaOpExecFunc =
    java.util.function.Function[(Int, Int), OperatorExecutor] with java.io.Serializable

  def generateJavaOpExec(
      opExecInitInfo: OpExecInitInfo,
      workerIdx: Int,
      numWorkers: Int
  ): OperatorExecutor = {
    opExecInitInfo match {
      case OpExecInitInfoWithCode(codeGen) =>
        val (code, language) =
          codeGen(workerIdx, numWorkers)
        language match{
          case "java" =>
            JavaRuntimeCompilation
              .compileCode(code)
              .getDeclaredConstructor()
              .newInstance()
              .asInstanceOf[OperatorExecutor]
          case "scala" =>
            ScalaRuntimeCompilation.compileCode(code)
              .getDeclaredConstructor()
              .newInstance()
              .asInstanceOf[OperatorExecutor]
          case other => ???
        }
      case OpExecInitInfoWithFunc(opGen) =>
        opGen(
          workerIdx,
          numWorkers
        )
    }
  }

  def apply(code: String, language: String): OpExecInitInfo =
    OpExecInitInfoWithCode((_, _) => (code, language))
  def apply(opExecFunc: OpExecFunc): OpExecInitInfo = OpExecInitInfoWithFunc(opExecFunc)
  def apply(opExecFunc: JavaOpExecFunc): OpExecInitInfo =
    OpExecInitInfoWithFunc((idx, totalWorkerCount) => opExecFunc.apply(idx, totalWorkerCount))
}

/**
  * Information regarding initializing an operator executor instance
  * it could be two cases:
  *   - OpExecInitInfoWithFunc:
  *       A function to create an operator executor instance, with parameters:
  *       1) the worker index, 2) the PhysicalOp;
  *   - OpExecInitInfoWithCode:
  *       A function returning the code string that to be compiled in a virtual machine.
  */
sealed trait OpExecInitInfo

final case class OpExecInitInfoWithCode(
    codeGen: (Int, Int) => (String, String)
) extends OpExecInitInfo
final case class OpExecInitInfoWithFunc(
    opGen: (Int, Int) => OperatorExecutor
) extends OpExecInitInfo

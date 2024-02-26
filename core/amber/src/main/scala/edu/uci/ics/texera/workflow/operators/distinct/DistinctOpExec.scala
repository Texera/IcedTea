package edu.uci.ics.texera.workflow.operators.distinct

import edu.uci.ics.amber.engine.common.InputExhausted
import edu.uci.ics.texera.workflow.common.operators.OperatorExecutor
import edu.uci.ics.texera.workflow.common.tuple.Tuple
import scala.collection.mutable

class DistinctOpExec extends OperatorExecutor {
  private val hashset: mutable.LinkedHashSet[Tuple] = mutable.LinkedHashSet()
  override def processTuple(
      tuple: Either[Tuple, InputExhausted],
      port: Int
  ): Iterator[Tuple] = {
    tuple match {
      case Left(t) =>
        hashset.add(t)
        Iterator()
      case Right(_) => hashset.iterator
    }
  }

  override def open(): Unit = {}

  override def close(): Unit = {}
}

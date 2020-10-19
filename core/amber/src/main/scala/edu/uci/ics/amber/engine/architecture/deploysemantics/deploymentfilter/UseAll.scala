package edu.uci.ics.amber.engine.architecture.deploysemantics.deploymentfilter
import edu.uci.ics.amber.engine.architecture.deploysemantics.layer.ActorLayer
import edu.uci.ics.amber.engine.operators.OpExecConfig
import akka.actor.Address

object UseAll {
  def apply() = new UseAll()
}

class UseAll extends DeploymentFilter {
  override def filter(
                       prev: Array[(OpExecConfig, ActorLayer)],
                       all: Array[Address],
                       local: Address
  ): Array[Address] = all
}

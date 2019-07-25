package misc.hello

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem, PoisonPill}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main extends App {
  val aList = List(1,2,3)
  val _system: ActorSystem = ActorSystem.create("hello-system")
  //val supervisor: ActorRef = _system.actorOf(SupervisorActor.props)
  val supervisor: ActorRef = _system.actorOf(SelfPipingSupervisorActor.props)

  supervisor ! Hello(5)
  supervisor ! Hello(2)
  supervisor ! Hello(3)
  supervisor ! Count()

  Thread.sleep(5000)

  supervisor ! PoisonPill
  _system.terminate
  Await.ready(_system.whenTerminated, Duration(1, TimeUnit.MINUTES))
}

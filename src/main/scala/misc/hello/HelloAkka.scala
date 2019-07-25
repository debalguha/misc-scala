package misc.hello

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem, PoisonPill}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object HelloAkka extends App {
  val _system: ActorSystem = ActorSystem.create("hello-system")
  val actor: ActorRef = _system.actorOf(GreetingsActor.props)

  actor ! Hello(5)

  Thread.sleep(2000)

  actor ! PoisonPill

  _system.terminate
  Await.ready(_system.whenTerminated, Duration(1, TimeUnit.MINUTES))
}

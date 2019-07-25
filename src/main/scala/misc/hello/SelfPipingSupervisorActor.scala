package misc.hello

import akka.actor.{Actor, ActorRef, Props}
import akka.util.Timeout

import akka.pattern.ask
import akka.pattern.pipe

import scala.concurrent.duration._

import scala.concurrent.ExecutionContext.Implicits.global

class SelfPipingSupervisorActor extends Actor {
  val greeter: ActorRef = context.actorOf(GreetingsActor.props)
  val counter: ActorRef = context.actorOf(CounterActor.props)

  override def receive: Receive = {
    case x: Hello => counter ! x
    case response: HelloResponse => println(response.response)

    case c: Count => {
      implicit val timeout = new Timeout(5 seconds)
      // ask the greeter for count
      val future = counter ? c
      pipe(future) to self
    }
    case c: CountResponse => println(s"Count Response: ${c}")
    case _ => unhandled()
  }

  def receiveWithoutCount: Receive = {
    case x: Hello => greeter ! x
    case response: HelloResponse => println(response.response)

    case c: Count => {
      implicit val timeout = new Timeout(1 seconds)
      // ask the greeter for count
      val future = greeter ? c
      pipe(future) to self
    }
    case c: CountResponse => println(s"Count Response: ${c}")
    case _ => unhandled()
  }
}

object SelfPipingSupervisorActor {
  def props = Props(classOf[SelfPipingSupervisorActor])
}
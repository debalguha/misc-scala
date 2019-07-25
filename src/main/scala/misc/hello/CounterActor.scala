package misc.hello

import akka.actor.{Actor, ActorRef, Props}

case class RealCount(count: Int)

class CounterActor extends Actor {

  val greeter: ActorRef = context.actorOf(DumbGreetingActor.props)
  def handleCountAndForward(countValue: Int): Receive = {
    case Count() => {
      greeter forward(RealCount(countValue))
    }
    case h: Hello => {
      greeter forward h
      context.become(handleCountAndForward(countValue + 1))
    }
  }

  override def receive: Receive = handleCountAndForward(0)
}

object CounterActor {
  def props = Props(classOf[CounterActor])
}

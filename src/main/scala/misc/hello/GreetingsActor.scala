package misc.hello

import akka.actor.{Actor, Props}

case class Hello(times: Int = 1)
case class HelloResponse(response: String)

class GreetingsActor extends Actor {

  var count: Int = 0

  override def receive: Receive = {
    case Hello(times) => { count += 1; sender ! HelloResponse( "Hello " * times ) }
    case RealCount(countVal) => sender ! CountResponse(countVal)
    case _ => unhandled()
  }
}

class DumbGreetingActor extends Actor {

  override def receive: Receive = {
    case Hello(times) => sender ! HelloResponse( "Hello " * times )
    case RealCount(countVal: Int) => sender ! CountResponse(countVal)
    case _ => unhandled()
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    super.preRestart(reason,message) // stops all children, calls postStop( ) for crashing actor
    println(s"actor restarting...")
  }

  override def postRestart(reason: Throwable): Unit = println(s"actor restarted...")

  override def postStop(): Unit = println(s"actor stopping...")
}

object GreetingsActor {
  def props = Props(classOf[GreetingsActor])
}

object DumbGreetingActor {
  def props = Props(classOf[DumbGreetingActor])
}
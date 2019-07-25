package misc.hello

import akka.actor.{Actor, Props}

class ImmutableGreetingActor extends Actor {

  def handleMessage(helloCounter: Int = 0): Receive = {
    case Hello(times) => {
        sender ! HelloResponse( "Hello " * times )
      context.become(handleMessage(helloCounter + 1))
      }
    case Count() => sender ! CountResponse(helloCounter)
    case _ => unhandled()
  }

  override def receive: Receive = {
    handleMessage(1)
  }
}

object ImmutableGreetingActor {
  val props = Props(classOf[ImmutableGreetingActor])
}

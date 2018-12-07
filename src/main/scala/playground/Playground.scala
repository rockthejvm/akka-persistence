package playground

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.PersistentActor

object Playground extends App {

  /**
    * A simple persistent actor that just logs all commands and events.
    */
  class SimplePersistentActor extends PersistentActor with ActorLogging {
    override def persistenceId: String = "simple-persistence"

    override def receiveCommand: Receive = {
      case message => log.info(s"Received: $message")
    }

    override def receiveRecover: Receive = {
      case event => log.info(s"Recovered: $event")
    }
  }

  val system = ActorSystem("Playground")
  val simpleActor = system.actorOf(Props[SimplePersistentActor], "simplePersistentActor")
  simpleActor ! "I love Akka!"

  /*
    If you're first starting with the project, just COMPILE the code (no need to run) to see that the libraries have correctly installed.
    Only run it after you've made the necessary configurations in application.conf.

    If the code compiles, you're good to go. Feel free to delete this code and go wild with your experiments with Akka Persistence!
  */
}

package part3_stores_serialization

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object Postgres extends App {

  val postgresActorSystem = ActorSystem("postgresSystem", ConfigFactory.load().getConfig("postgresDemo"))
  val persistentActor = postgresActorSystem.actorOf(Props[SimplePersistentActor], "simplePersistentActor")

  for (i <- 1 to 10) {
    persistentActor ! s"I love Akka [$i]"
  }
  persistentActor ! "print"
  persistentActor ! "snap"

  for (i <- 11 to 20) {
    persistentActor ! s"I love Akka [$i]"
  }
}

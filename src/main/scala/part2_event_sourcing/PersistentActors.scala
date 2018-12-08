package part2_event_sourcing

import java.util.Date

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.PersistentActor


object PersistentActors extends App {

  /*
    Scenario: we have a business and an accountant which keeps track of our invoices.
   */

  // COMMANDS
  case class Invoice(recipient: String, date: Date, amount: Int)

  // EVENTS
  case class InvoiceRecorded(id: Int, recipient: String, date: Date, amount: Int)

  class Accountant extends PersistentActor with ActorLogging {

    var latestInvoiceId = 0
    var totalAmount = 0

    override def persistenceId: String = "simple-accountant" // best practice: make it unique

    /**
      * The "normal" receive method
      */
    override def receiveCommand: Receive = {
      case Invoice(recipient, date, amount) =>
        /*
          When you receive a command
          1) you create an EVENT to persist into the store
          2) you persist the event, the pass in a callback that will get triggered once the event is written
          3) we update the actor's state when the event has persisted
         */
        log.info(s"Receive invoice for amount: $amount")
        persist(InvoiceRecorded(latestInvoiceId, recipient, date, amount))
          /* time gap: all other messages sent to this actor are STASHED */
        { e =>
          // SAFE to access mutable state here

          // update state
          latestInvoiceId += 1
          totalAmount += amount

          // correctly identify the sender of the COMMAND
          sender() ! "PersistenceACK"
          log.info(s"Persisted $e as invoice #${e.id}, for total amount $totalAmount")
        }
      // act like a normal actor
      case "print" =>
        log.info(s"Latest invoice id: $latestInvoiceId, total amount: $totalAmount")
    }

    /**
      Handler that will be called on recovery
     */
    override def receiveRecover: Receive = {
      /*
        best practice: follow the logic in the persist steps of receiveCommand
       */
      case InvoiceRecorded(id, _, _, amount) =>
        latestInvoiceId = id
        totalAmount += amount
        log.info(s"Recovered invoice #$id for amount $amount, total amount: $totalAmount")
    }
  }

  val system = ActorSystem("PersistentActors")
  val accountant = system.actorOf(Props[Accountant], "simpleAccountant")

//  for (i <- 1 to 10) {
//    accountant ! Invoice("The Sofa Company", new Date, i * 1000)
//  }

}

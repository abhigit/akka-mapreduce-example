package wordcount

import akka.actor.actorRef2Scala
import akka.actor.ActorSystem
import akka.actor.Props
import akka.dispatch.Await

import wordcount.actors.Master

import akka.pattern.ask
import akka.util.duration.intToDurationInt
import akka.util.Timeout

import scala.collection.mutable.ArrayBuffer
import scala.collection.immutable.Map

// a sealed marker trait for messages used in the application
sealed trait Message

// a message for holding a word and its frequency
// master actor will use it to activate mapper actor
case class WordCount(word : String, count :  Int) extends Message

// a message which mapper actor produces

case class MappedWordCount(list : ArrayBuffer[WordCount]) extends Message

// a message reducer sends to aggregator actor by reducing mapped data

case class ReducedWordCount(map : Map[String,Int]) extends Message

// a simple message passed by maser actor to aggregator actor to obtain aggregated result

case class GetResult extends Message


object WordCountApplication extends App {
        val sys = ActorSystem("WordCount")
        val master = sys.actorOf(Props[Master], name = "master")
        implicit val timeout = Timeout(5 seconds)

        master ! "scala is interesting!"

        master ! "akka with scala is fun!"

        master ! "I understood Actors with akka"


       Thread.sleep(500)
       val future = (master ? GetResult).mapTo[String]
       val result = Await.result(future, timeout.duration)
       println(result)
       sys.shutdown

}

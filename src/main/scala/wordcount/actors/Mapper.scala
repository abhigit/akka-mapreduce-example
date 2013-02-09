package wordcount.actors

import akka.actor.Actor
import akka.actor.ActorRef
import wordcount.MappedWordCount
import wordcount.WordCount
import scala.collection.mutable.ArrayBuffer


// a mapper receives a string as message and passes MappedWordCount
class Mapper extends Actor {
    def receive : Receive = {
        case message : String => {
             val m = mapped(message)
             //println(m)
             sender ! m 
        }
    }
    private def mapped(message : String) : MappedWordCount = MappedWordCount {
        message.split("""\s+""").foldLeft(ArrayBuffer.empty[WordCount]) {
           (buffer, word) => buffer += WordCount(word.toLowerCase,1)
        }
    }

}

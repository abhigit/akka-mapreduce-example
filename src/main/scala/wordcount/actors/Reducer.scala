package wordcount.actors

import akka.actor.Actor
import akka.actor.ActorRef
import wordcount.MappedWordCount
import wordcount.ReducedWordCount
import wordcount.WordCount
import scala.collection.mutable.ArrayBuffer
import scala.collection.immutable.Map


// a reducer receives list of MappedWordCount and gives ReducedWordCount
class Reducer extends Actor {
    def receive : Receive = {
        case  MappedWordCount(list) => sender ! reducer(list)
    }
    private def reducer(list : ArrayBuffer[WordCount]) : ReducedWordCount = ReducedWordCount {
        list.foldLeft(Map.empty[String,Int]) {
            (buffer, wordCount) => if(buffer contains wordCount.word) buffer + (wordCount.word -> (buffer.get(wordCount.word).get + 1))
            else buffer + (wordCount.word -> 1)
        }
    }

}

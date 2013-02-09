package wordcount.actors


import scala.collection.immutable.Map
import scala.collection.mutable.HashMap

import akka.actor.Actor
import wordcount.GetResult
import wordcount.ReducedWordCount


// an aggregator which will aggregate the reduced WordCounts

class Aggregator extends Actor {
        val result = new HashMap[String,Int]

        def receive : Receive = {
            case ReducedWordCount(data) => {
                   aggregated(data)	
            }
            case GetResult => sender ! result.toString
        }

        def aggregated(map :Map[String,Int]) : Unit ={
            for ((key, value) <- map) { 
                if(result contains key) result(key) = (value + result.get(key).get)
                else result += (key -> value)
            } 

        }
}

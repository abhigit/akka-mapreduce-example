package wordcount.actors


import scala.collection.immutable.Map
import scala.collection.mutable.HashMap

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.routing.RoundRobinRouter

import wordcount.GetResult
import wordcount.ReducedWordCount
import wordcount.MappedWordCount


// a master which acts like a Supervisor actor
// it instantiates child actors and acts as a gateway for all
// messages that are passed around in the ActorSystem

class Master extends Actor {
        val mapper = context.actorOf(Props[Mapper].withRouter(
                     RoundRobinRouter(nrOfInstances = 5)),name = "map") 
        val reducer = context.actorOf(Props[Reducer].withRouter(
                     RoundRobinRouter(nrOfInstances = 5)),name = "reduce") 
	// we need only one aggregator
        val aggregator = context.actorOf(Props[Aggregator] ,name = "aggregate") 

        def receive : Receive = {
            case message : String => { mapper ! message }
            case data : MappedWordCount => reducer ! data 
            case data1 : ReducedWordCount => aggregator ! data1
            case GetResult => aggregator forward GetResult
        }
}

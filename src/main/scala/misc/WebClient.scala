package misc

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

import scala.concurrent.Future
import scala.util.{Failure, Success}
import akka.http.scaladsl.unmarshalling.Unmarshal
import spray.json._
import spray.json.DefaultJsonProtocol._

object WebClient {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit object AnyJsonFormat extends JsonFormat[Any] {
    def write(x: Any) = x match {
      case n: Int => JsNumber(n)
      case s: String => JsString(s)
      case b: Boolean if b == true => JsTrue
      case b: Boolean if b == false => JsFalse
    }
    def read(value: JsValue) = value match {
      case JsNumber(n) => n.intValue()
      case JsString(s) => s
      case JsTrue => true
      case JsFalse => false
    }
  }

  def execSingleRequest(uriBase: String, params: Map[String, String]) = {
    val uri = Uri(uriBase).withQuery(Query(params))
    Http().singleRequest(HttpRequest().withUri(uri)) flatMap {
      response => {
        Unmarshal(response.entity).to[String]
      }
    }
  }

  def main(args: Array[String]): Unit = {
    //doLowLevel
    doRequestLevel
  }

  def doRequestLevel() = {
    val uri = Uri("http://calculatorapp.infochoice.com.au/calculationService/HomeLoanStampDuty").withQuery(Query(
      "callback" -> " ",
      "State" -> "NSW",
      "PropertyValue" -> "669000",
      "LoanAmount" -> "300000",
      "PropertyUse" -> "Residential",
      "PropertyDescription" -> "EstablishedHome",
      "IsFirstHomeBuyer" -> "true",
      "PropertyLocation" -> "InfochoiceHomeLoans",
      "ClientName" -> "InfochoiceHomeLoans",
      "IsCustomNotesEnabled" -> "false",
      "_" -> "1566302708186"
    ))
    val responseFuture: Future[String] = Http().singleRequest(HttpRequest().withUri(uri)) flatMap {
      response => {
        Unmarshal(response.entity).to[String]
      }
    }
    responseFuture.andThen {
      case Success(x: String) => {
        println(x)
        val json = x.drop(2).dropRight(2).parseJson.convertTo[Map[String, JsValue]]
        //val json = x.drop(2).dropRight(2).toJson
        println(json)
      }
      case Failure(x: Throwable) => x.printStackTrace
    } andThen {
      case _ => system.terminate()
    }
  }
  def doLowLevel() = {
    val connectionFlow: Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] = Http().outgoingConnection("akka.io")
    val responseFuture: Future[HttpResponse] = Source.single(HttpRequest(uri = "/"))
        .via(connectionFlow)
        .runWith(Sink.head)

    responseFuture.andThen {
      case Success(_) => println("request succeded")
      case Failure(_) => println("request failed")
    }.andThen {
      case _ => system.terminate()
    }
  }
}
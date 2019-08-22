package misc.loan.stampduty

import misc.WebClient
import spray.json._
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Await
import scala.concurrent.duration._

case class StampDutyDetail(mortgageregFee: Double, transferFee: Double, stampDuty: Double, totalGovernmentFees: Double, firstHomeBuyerGrant: Double, otherConcessions: Double, totalGovernmentConcessions: Double)
object StampDutyCalculator {
  implicit def jsValueToNumber(value: JsValue) = value match {
    case JsNumber(n) => n.doubleValue()
    case _ => 0d
  }
  implicit def mapToStampDutyDetail(jsonMap: Map[String, JsValue]): StampDutyDetail = {
    StampDutyDetail(
      jsonMap.getOrElse("MortgageregFee", JsNumber(0)).toDouble, jsonMap.getOrElse("TransferFee", JsNumber(0)).toDouble, jsonMap.getOrElse("StampDuty", JsNumber(0)).toDouble,
      jsonMap.getOrElse("TotalGovernmentFees", JsNumber(0)).toDouble, jsonMap.getOrElse("FirstHomeBuyerGrant", JsNumber(0)).toDouble, jsonMap.getOrElse("OtherConcessions", JsNumber(0)).toDouble,
      jsonMap.getOrElse("TotalGovernmentConcessions", JsNumber(0)).toDouble
    )
  }
  implicit object AnyJsonFormat extends JsonFormat[Any] {
    def write(x: Any) = x match {
      case n: Int => JsNumber(n)
      case s: String => JsString(s)
      case b: Boolean if b == true => JsTrue
      case b: Boolean if b == false => JsFalse
    }
    def read(value: JsValue) = value match {
      case JsNumber(n) => n.doubleValue()
      case JsString(s) => s
      case JsTrue => true
      case JsFalse => false
    }
  }
  def dutyFor(capital: Long, firstHomeBuyer: Boolean = false): StampDutyDetail = {
    val params = Map("callback" -> " ",
      "State" -> "NSW",
      "PropertyValue" -> s"$capital",
      "LoanAmount" -> "300000",
      "PropertyUse" -> "Residential",
      "PropertyDescription" -> "EstablishedHome",
      "IsFirstHomeBuyer" -> s"${firstHomeBuyer}",
      "PropertyLocation" -> "InfochoiceHomeLoans",
      "ClientName" -> "InfochoiceHomeLoans",
      "IsCustomNotesEnabled" -> "false",
      "_" -> "1566302708186")
    Await.result(WebClient.execSingleRequest("http://calculatorapp.infochoice.com.au/calculationService/HomeLoanStampDuty", params), 5 seconds)
  }.drop(2).dropRight(2).parseJson.convertTo[Map[String, JsValue]].-("RevenueOfficeLinks") - ("Notes") - ("OtherConcessionTitle")

  def dutyFor_1(capital: Long, firstHomeBuyer: Boolean = false): Double = {
    if(!firstHomeBuyer){
      calculate(capital)
    } else {
      capital match {
        case x if x <= 650000 => 0
        case x if x <= 799999 => calculate(799999 - x)
        case _ => calculate(capital)
      }
      /*if(capital <= 650000){
        0
      } else {
        calculate(capital - 650000)
      }*/
    }
  }
  private def calculate(capital: Long) = capital match {
    case x if x <= 14000 => (x/100) * 1.25
    case x if x > 14000 && x <= 30000 => 175 + ((x -14000)/100) * 1.50
    case x if x > 30000 && x <= 81000 => 415 + ((x -30000)/10) * 1.75
    case x if x > 81000 && x <= 304000 => 1307 + ((x -81000)/100) * 3.50
    case x if x > 304000 && x <= 1013000 => 9112 + ((x -304000)/100) * 4.50
    case x if x > 1013000 => 41017 + ((x -1013000)/100) * 4.50
    case _ => 0d
  }
}

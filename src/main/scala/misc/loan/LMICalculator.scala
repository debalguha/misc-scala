package misc.loan

import java.io.File

import misc.loan.lmi.LMILookupTable
import misc.loan.stampduty.StampDutyCalculator

import scala.io.Source

object LMICalculator extends App {
  val file = new File("src/test/resources/lmi.txt")
  val lmiFileContent = Source.fromFile(file).mkString
  val lookupTable = LMILookupTable(lmiFileContent.split("\n", -1))
  println(lookupTable.lookupLMIFor(650000L, 90))
  println(StampDutyCalculator.dutyFor(650000L, true))
  println(StampDutyCalculator.dutyFor(700000L, true))
}

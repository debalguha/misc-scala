package misc.loan.lmi

import java.io.File

import misc.loan.stampduty.StampDutyCalculator

import scala.io.Source

object LMICalculator {
  val file = new File("src/test/resources/lmi.txt")
  val lmiFileContent = Source.fromFile(file).mkString
  val lookupTable = LMILookupTable(lmiFileContent.split("\n", -1))

  def calculateLMIFor(capital: Long, percentLoan: Int) = {
    lookupTable.lookupLMIFor(capital, percentLoan)
  }
  /*def calculateLMIFor(capital: Long): Array[Double] = {
    val loanRange = Range(81, 94, 1)
    loanRange.map(lookupTable.lookupLMIFor(capital, _)).toArray
  }*/
  println(lookupTable.lookupLMIFor(650000L, 90))
  println(StampDutyCalculator.dutyFor(650000L, true))
  println(StampDutyCalculator.dutyFor(700000L, true))
}

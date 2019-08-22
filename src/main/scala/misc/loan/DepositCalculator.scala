package misc.loan

import misc.loan.lmi.LMICalculator
import misc.loan.stampduty.StampDutyCalculator

object DepositCalculator {
  def calculateMinimumDeposit(capital: Long, loanPercent: Int, firstHomeBuyer: Boolean = false): Double = {
    val shouldIncludeLMIInLoan = loanPercent > 90
    val lmiAmount = LMICalculator.calculateLMIFor(capital, loanPercent)
    val stampDuty = StampDutyCalculator.dutyFor(capital, firstHomeBuyer)

  }
}

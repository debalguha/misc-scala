package misc.loan.stampduty

object StampDutyCalculator {
  def dutyFor(capital: Long, firstHomeBuyer: Boolean = false): Double = {
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

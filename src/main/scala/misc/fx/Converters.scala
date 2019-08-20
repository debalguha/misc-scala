package misc.fx

import java.util.Currency

abstract sealed trait Converter {
  def convert(value: Double): Double

  val currencyPair: (Currency, Currency)
}

case class FXEntry(fomCurrency: Currency, toCurrency: Currency, factorOrCurrency: Option[Either[Double, Currency]])

class CurrencyConverter(pair: (Currency, Currency), val func: Double => Double, refPair: (Currency, Currency)) extends Converter {
  override val currencyPair = pair

  override def convert(value: Double): Double = func(value)
}

object CurrencyConverter {
  val idFunc = (x: Double) => x
  val conversionWithRate = (rate: Double, value: Double) => rate * value
  val exceptFunc = (x: Double) => throw new IllegalStateException()

  def apply(pair: (Currency, Currency)): CurrencyConverter = {
    new CurrencyConverter(pair, idFunc, null)
  }

  def apply(pair: (Currency, Currency), func: Double => Double): CurrencyConverter = {
    new CurrencyConverter(pair, func, null)
  }

  def apply(pair: (Currency, Currency), conversionRate: Double): CurrencyConverter = {
    new CurrencyConverter(pair, conversionWithRate(conversionRate, _), null)
  }

  def apply(pair: (Currency, Currency), refPair: (Currency, Currency)): CurrencyConverter = {
    new CurrencyConverter(pair, idFunc, refPair)
  }

  def apply(pair: (Currency, Currency), except: IllegalStateException): CurrencyConverter = {
    new CurrencyConverter(pair, exceptFunc, null)
  }

  def apply(pair: (Currency, Currency), leftConverter: Converter, rightConverter: Converter): CurrencyConverter = {
    if (leftConverter.asInstanceOf[CurrencyConverter].func == exceptFunc || rightConverter.asInstanceOf[CurrencyConverter].func == exceptFunc) {
      new CurrencyConverter(pair, exceptFunc, null)
    } else {
      new CurrencyConverter(pair, composeConverters(leftConverter, rightConverter), null)
    }
  }

  private def composeConverters(leftConverter: Converter, rightConverter: Converter) = {
    rightConverter.asInstanceOf[CurrencyConverter].func.compose(leftConverter.asInstanceOf[CurrencyConverter].func)
  }
}

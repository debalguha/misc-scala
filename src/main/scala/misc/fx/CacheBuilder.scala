package misc.fx


object CacheBuilder {
  type currencyPairTuple = (java.util.Currency, java.util.Currency)
  type entryMap = Map[currencyPairTuple, FXEntry]
  type cacheType = Cache[currencyPairTuple, Converter]

  /*def buildCacheFromFXEntries(fxEntries: entryMap): cacheType = {
    val seedCache: cacheType = InMemoryConcurrentMapCache()
    fxEntries.filter(_._2.factorOrCurrency nonEmpty).keys.foreach(key => {
      val fxEntry = fxEntries.get(key).get
      if (fxEntry.factorOrCurrency.get.isLeft) {
        CurrencyConverter(key, fxEntry.factorOrCurrency.get.left.get)
      }
    })
  }*/

  def buildCurrencyConverter(fxEntry: FXEntry, allEntries: entryMap, converterCache: cacheType, visitedPairs: Set[currencyPairTuple] = Set()): Converter = {
    val currencyPair: currencyPairTuple = (fxEntry.fomCurrency, fxEntry.toCurrency)
    if (fxEntry.factorOrCurrency.get.isLeft) {
      CurrencyConverter(currencyPair, fxEntry.factorOrCurrency.get.left.get)
    } else {
      val leftCurrencyPair: currencyPairTuple = (fxEntry.fomCurrency, fxEntry.factorOrCurrency.get.right.get)
      val rightCurrencyPair: currencyPairTuple = (fxEntry.factorOrCurrency.get.right.get, fxEntry.fomCurrency)

      if (canDetectCyclicReference(leftCurrencyPair, rightCurrencyPair, visitedPairs)) {
        CurrencyConverter((fxEntry.fomCurrency, fxEntry.toCurrency), new IllegalStateException(s"Currency pair ${(fxEntry.fomCurrency, fxEntry.toCurrency)} has cyclic reference to ${fxEntry.factorOrCurrency.get.right.get}"))
      } else {
        val leftConverter = buildCurrencyConverter(allEntries.get(leftCurrencyPair).get, allEntries, converterCache, visitedPairs + currencyPair)
        val rightConverter = buildCurrencyConverter(allEntries.get(rightCurrencyPair).get, allEntries, converterCache, visitedPairs + currencyPair)

        CurrencyConverter((fxEntry.fomCurrency, fxEntry.toCurrency), leftConverter, rightConverter)
      }
    }
  }

  def canDetectCyclicReference(leftCurrencyPair: currencyPairTuple, rightCurrencyPair: currencyPairTuple, visitedPairs: Set[currencyPairTuple]) = {
    visitedPairs.contains(leftCurrencyPair) || visitedPairs.contains(rightCurrencyPair)
  }
}

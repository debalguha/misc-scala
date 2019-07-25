package misc.fx

import java.util.concurrent.ConcurrentHashMap

trait Cache[K, V] {
  abstract def addToCache(k: K, t: V): Cache[K, V]
  abstract def lookupCache(k: K): Option[V]
}
case class Converter(pair: (String, String), func: Double => Double, refPair: (String, String))
case class FXEntry(fomCurrency: String, toCurrency: String, factorOrCurrency: Either[Double, String])

class InMemoryConcurrentMapCache(mapCache: ConcurrentHashMap[(String, String), Converter]) extends Cache[(String, String), Converter] {

  override def addToCache(k: (String, String), v: Converter): Cache[(String, String), Converter] = {
    val newMapCache = new ConcurrentHashMap(this.mapCache);
    newMapCache.putIfAbsent(k, v)
    new InMemoryConcurrentMapCache(newMapCache)
  }

  override def lookupCache(k: (String, String)): Option[Converter] = {
    Some(mapCache.get(k))
  }
}

object InMemoryConcurrentMapCache {
  def apply() = {
    new InMemoryConcurrentMapCache(new ConcurrentHashMap[(String, String), Converter]());
  }
}

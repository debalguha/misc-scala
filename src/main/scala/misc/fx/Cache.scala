package misc.fx

import java.util.Currency

import scala.collection.concurrent.TrieMap

trait Cache[K, V] {
  def addToCache(k: K, t: V): Cache[K, V]
  def lookupCache(k: K): Option[V]
  def contains(k: K): Boolean
}

class InMemoryConcurrentMapCache(mapCache: scala.collection.concurrent.Map[(Currency, Currency), Converter]) extends Cache[(Currency, Currency), Converter] {

  override def addToCache(k: (Currency, Currency), v: Converter): Cache[(Currency, Currency), Converter] = {
    new InMemoryConcurrentMapCache(mapCache += ((k, v)))
  }

  override def lookupCache(k: (Currency, Currency)): Option[Converter] = {
    mapCache.get(k)
  }

  override def contains(k: (Currency, Currency)): Boolean = mapCache.contains(k)
}

object InMemoryConcurrentMapCache {
  def apply() = {
    new InMemoryConcurrentMapCache(new TrieMap[(Currency, Currency), Converter]());
  }
}

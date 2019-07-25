package misc.category

import org.scalacheck._

object CategorySpecWithScalaCheckOnly extends Properties("Category") {
  import Category._
  import Prop.forAll
  val f = (i: Int) => i.toString
  val g = (s: String) => s.length
  val h = (i: Int) => i * i

  property("composition") = forAll {(i: Int) => compose(h, compose(g, f))(i) == compose(compose(h, g), f)(i) }
  property("identity") = forAll {(i: Int) => compose(f, id[Int])(i) == compose(id[String], f)(i) }
}

package misc.category

import org.scalacheck.{Prop, Properties}
import org.specs2.{ScalaCheck, Specification}

class CategorySpec extends Specification with ScalaCheck {

  val f = (i: Int) => i.toString
  val g = (s: String) => s.length
  val h = (i: Int) => i * i
  val idFunc = (x: Int) => Math.pow(x, 0).toInt

  val p1: Prop = Prop.forAll { (a: Int) => a + a == 2 * a }

  s2"addition and multiplication are related $p1"

  val p2: Properties = new Properties("addition/multiplication") {
    property("addition1") = Prop.forAll { (a: Int) => a + a == 2 * a }
    property("addition2") = Prop.forAll { (a: Int) => a + a + a == 3 * a }
  }

  s2"addition and multiplication are related $p2"

  def is = s2"""
      A Category must
        satisfy associativity in    ${assocCheck}
        saisfy identity in          $identityCheck
    """
  import Category._

  def assocCheck = {
    Prop forAll {
      (i: Int) => {
        compose(h, compose(g, f))(i) == compose(compose(h, g), f)(i)
      }
    }
  }

  def identityCheck = {
    Prop forAll { (i: Int) =>
      compose(f, id[Int])(i) mustEqual compose(id[String], f)(i)
      compose(h, idFunc)(i) mustEqual compose(idFunc, h)(i)
    }
  }
}

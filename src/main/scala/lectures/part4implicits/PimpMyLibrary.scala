package lectures.part4implicits

import javax.swing.text.StyledEditorKit.BoldAction

/**
  * Created by soner.guzeloglu onn 28.08.2019
  */
object PimpMyLibrary extends App {

  // 2.isPrime

  implicit class RichInt(val value: Int) extends AnyVal {
    def isEven: Boolean = value % 2 == 0

    def sqrt: Double = Math.sqrt(value)

    def times(f: () => Unit): Unit = {
      def timesAux(n: Int): Unit =
        if (n < 0) ()
        else {
          f()
          timesAux(n - 1)
        }

      timesAux(value)
    }

    def *[T](list: List[T]): List[T] = {
      def concatenate(n: Int): List[T] =
        if (n <= 0) List()
        else concatenate(n - 1) ++ list

      concatenate(value)
    }
  }

  implicit class RicherInt(richInt: RichInt) {
    def isOdd: Boolean = richInt.value % 2 != 0
  }

  new RichInt(42).sqrt

  42.isEven // new RichInt(42).isEven
  // Type Enrichment = pimping

  import scala.concurrent.duration._

  3.seconds

  // compiler does not do multiple implicit searches
  // 42.isOdd

  /*
      Enrich the String class
        - asInt
        - encrpyt
          John -> Lnjp

        Keep enriching the Int class
          - times(function)
            3.times(() => ..)
          =
            3 * List(1,2) => List(1,2,1,2,1,2)
   */

  // 1
  implicit class RichString(val str: String) extends AnyVal {
    def asINt: Int = Integer.valueOf(str) // java.lang.Integer -> Int
    def encrpyt(cypherDistance: Int): String = str.map(c => (c + cypherDistance).asInstanceOf[Char])
  }

  println("3".asINt + 4)
  println("John".encrpyt(2))

  3.times(()=> println("scala rocks"))

  println(4 * List(1,2))

  // "3" / 4
  implicit def stringToInt(string: String): Int = Integer.valueOf(string)
  println("6" / 2) // stringToInt("6") / 2


  // equivalent: implicit class RichAltInt(value)
  class RichAltInt(value: Int)
  implicit def enrich(value: Int): RichAltInt = new RichAltInt(value)



  // danger zone
  implicit def intToBoolean(i: Int): Boolean = i == 1

  val aConditionValue = if (3) "OK" else "Something wrog"
  println(aConditionValue)
}

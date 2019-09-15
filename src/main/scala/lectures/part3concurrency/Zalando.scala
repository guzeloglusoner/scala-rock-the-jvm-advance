import scala.collection.immutable.Stream.Cons


object Zalando  extends App {
  def solution(a: Int, b: Int): Int = {
    //
    consecutiveProducts(0, 1).takeWhile(_ <= b).count(_ >= a)
  }

  // creates Stream of Cons
  def consecutiveProducts(first: Int, second: Int): Stream[Int] =
    new Cons(first * second, consecutiveProducts(second, second + 1))

  println(solution(50000000, 100000000))
}

object Zalando2 {
  def solution(n: Int): Int = n match {
    case x if x < 10 => 0
    case _ => scala.math.pow(10, n.toString.length - 1).toInt
  }
}


package lectures.part2afp

/**
  * Created by soner.guzeloglu onn 20.08.2019
  */
object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1 //  Function1[Int, Int] === Int => Int

  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException


  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }
  // {1,2,5} => Int

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  } // partial function value

  println(aPartialFunction(2))
  // println(aPartialFunction(1928312))

  // PF Utilities

  println(aPartialFunction.isDefinedAt(67))

  // can be lift to total function

  val lifted = aPartialFunction.lift // Int => Option[Int]
  println(lifted(2))
  println(lifted(12983))

  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 512
  }

  println(pfChain(2))
  println(pfChain(45))

  // PF extend normal functions

  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  // HOFs accept partial functions as well
  val aMappedList = List(1, 2, 3).map {
    case 1 => 42
    case 2 => 78
    case 3 => 100
  }
  println(aMappedList)

  /*
  * Note: PF can only have ONE parameter type
  *
  * */

  /** Exercises
    *
    * 1- construct a PF instance yourself (anonymous class)
    * 2- dumb chatbot as a PF
    * */

  val aManualFussyFunction = new PartialFunction[Int, Int] {
    override def isDefinedAt(x: Int): Boolean = x == 1 || x == 2 || x == 5

    override def apply(v1: Int): Int = v1 match {
      case 1 => 42
      case 2 => 56
      case 5 => 999
    }
  }

  val aChatBot: PartialFunction[String, String] = {
    case "Hello" => "Hi"
    case "How are you?" => "Thanks, what about you?"
    case "Goodbye" => ":)"
  }

  scala.io.Source.stdin.getLines().map(aChatBot).foreach(println)

}

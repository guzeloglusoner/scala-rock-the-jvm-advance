package lectures.part2afp

/**
  * Created by soner.guzeloglu onn 20.08.2019
  */
object CurriesPAF extends App {

  // curried functions
  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3 = superAdder(3) // Int => Int = y => 3 + y

  println(add3(5))
  println(superAdder(3)(5)) // curried function

  //curried methods
  def curriedAdder(x: Int)(y: Int): Int = x + y

  val add4: Int => Int = curriedAdder(4)

  // lífting: transforming a method to functions = ETA-EXPANSION
  // functions != methods (JVM limitation)
  def inc(x: Int) = x + 1

  List(1, 2, 3).map(x => inc(x)) // ETA-Expansion

  // Partial Function applications
  val add5 = curriedAdder(5) _ // Int => Int

  // EXERCISE
  val simpleAddFunction = (x: Int, y: Int) => x + y

  def simpleAddMethod(x: Int, y: Int) = x + y

  def curriedAddMethod(x: Int)(y: Int) = x + y

  // add7: Int => Int = y => 7 + y
  // as many different implementations of add7 using above.

  val add7 = (x: Int) => simpleAddFunction(7, x) // simplest 3
  val add7_2 = simpleAddFunction.curried(7)
  val add7_6 = simpleAddFunction(7, _: Int) // works as well
  val add7_3 = curriedAddMethod(7) _ // PAF
  val add7_4 = curriedAddMethod(7)(_) // PAF = alternative syntax

  val add7_5 = simpleAddMethod(7, _: Int) // alternative syntax for turning methods into function values
  // y => simpleAddMethod(7 , y)
  val curriedAddMethod7 = curriedAddMethod(7) _

  // underscores are powerful
  def concatenator(a: String, b: String, c: String) = a + b + c
  val insertName = concatenator("Hello, I am", _:String, "how are you?") // x:String => concatenator(hello, x, howareyou)
  println(insertName("Soner"))

  val fillInTheBlanks = concatenator("Hello, ",_: String, _: String)
  println(fillInTheBlanks("Daniel", "Scala is great"))

  /*
  1. Process a list of numbers and return their string representations with different formates
    Use the %4/2f, %8.6f and %14.12f with a curried formatter function
   */
  println("%8.6f".format(Math.PI))

  /*val formatter: String => Double => String =
    x => y => x.format(y)
*/
// this is a curied method and it can be easily be used partially applied functions with underscores
  def formatter(s: String)(number: Double): String = s.format(number)
  val l = List.fill(5)(math.random())

  //l.map(formatter("%4.2f")).foreach(println)

  val simpleFormat = formatter("%4.2f") _ // lift
  val seriousFormat = formatter("%8.6f") _
  val preciseFormat = formatter("%14.12f") _

  println(l.map(preciseFormat)) // compiler does sweet ETA-EXPANSION for use
  /*
  2. difference between
      - functions vs methods
      - parameters: by-name vs 0-lambda
   */

  def byName(n: => Int): Int = n + 1
  def byFunction(f: () => Int) = f() + 1
  def method: Int = 42
  def parenMethod(): Int = 42
  /*
  calling byName and byFunction
    - int
    - method
    - parenMethod
    - lambda
    - PAF
   */

  byName(23)
  byName(method)
  byName(parenMethod())
  byName(parenMethod) // ok but beware => byName(parenMethod())
//  byName(() => 42)
  byName((() => 42)()) // ok
//  byName(parenMethod _) not ok

//  byFunction(45)
//  byFunction(method) not ok!!!!!! parameterless methods does not go under ETA-EXPANSION
  byFunction(parenMethod) // compiler does ETA-Expansion
  byFunction(() => 46) // works
  byFunction(parenMethod _) // also works


}

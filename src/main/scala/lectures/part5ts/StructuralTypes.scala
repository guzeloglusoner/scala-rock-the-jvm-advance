package lectures.part5ts

/**
  * Created by soner.guzeloglu onn 15.09.2019
  */
object StructuralTypes extends App {

  // structural types
  type JavaCloseable = java.io.Closeable

  class HipsterCloseable {
    def close(): Unit = println("yeah yeah I am closing")

    def closeSilently(): Unit = println("not making a sound")
  }

  //  def closeQuitely(closeable: JavaCloseable OR HipsterCloseable) // ?!

  type UnifiedCloseable = {
    def close(): Unit
  } // Structural type

  def closeQuitely(unifiedCloseable: UnifiedCloseable): Unit = unifiedCloseable.close()

  closeQuitely(new JavaCloseable {
    override def close(): Unit = ???
  })

  closeQuitely(new HipsterCloseable)

  // TYPE REFINEMENTS

  type AdvancedCloseable = JavaCloseable {
    def closeSilently(): Unit
  }

  class AdvancedJavaCloseable extends JavaCloseable {
    override def close(): Unit = println("Java closes")

    def closeSilently(): Unit = println("Java closes silently")
  }

  def closeShh(advCloseable: AdvancedCloseable): Unit = advCloseable.closeSilently()

  closeShh(new AdvancedJavaCloseable)
  //  closeShh(new HipsterCloseable)

  // using structural types as standalone types
  def altClose(closeable: {def close(): Unit}) = closeable.close()

  // type-checking => duck typing

  type SoundMaker = {
    def makeSound(): Unit
  }

  class Dog {
    def makeSound(): Unit = println("bark!")
  }

  class Car {
    def makeSound(): Unit = println("vroom")
  }

  val dog: SoundMaker = new Dog
  val car: SoundMaker = new Car

  // static duck typing

  // CAVEAT: based on reflection

  /*
    exercises:

   */
  trait CBL[+T] {
    def head: T

    def tail: CBL[T]
  }

  class Human {
    def head: Brain = new Brain
  }

  class Brain {
    override def toString: String = "BRAINZ!"
  }

  def f[T](somethingWithAHead: {def head: T}): Unit = println(somethingWithAHead.head)

  /*
  * f is compatible with CBL or with a Human? yes
  * */

  case object CBNil extends CBL[Nothing] {
    override def head: Nothing = ???

    override def tail: CBL[Nothing] = ???
  }

  case class CBCons[T](override val head: T, override val tail: CBL[T]) extends CBL[T]

  f(CBCons(2, CBNil))
  f(new Human) // ?! T = Brain !! Compiler understands
  /*
    2. is compatible with CBL or with a Human?
   */

  object HeadEqualizer {
    type Headable[T] = {def head: T}

    def ===[T](a: Headable[T], b: Headable[T]): Boolean = a.head == b.head
  }

  val brainzList = CBCons(new Brain, CBNil)

  val stringList = CBCons("Brainz", CBNil)
  HeadEqualizer.===(brainzList, new Human)
  // problem?
  HeadEqualizer.===(new Human, stringList) // not type safe
}

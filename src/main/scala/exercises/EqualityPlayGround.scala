package exercises

import lectures.part4implicits.TypeClasses.User

/**
  * Created by soner.guzeloglu onn 28.08.2019
  */
object EqualityPlayGround extends App {

  /*
    Equality Type Class
   */

  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }

  implicit object NameEquality extends Equal[User] {
    override def apply(user1: User, user2: User): Boolean = user1.name == user2.name
  }

  object FullEquality extends Equal[User] {
    override def apply(user1: User, user2: User): Boolean = user1.name == user2.name && user1.email == user2.email
  }

  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]) = equalizer.apply(a, b)
  }

  val john = User("John", 32, "john@rockthejvm.cm")
  val anotherJohn = User("John", 45, "anotherJohn@gmail.com")

  // AD-HOC polymorpishm

  /*
    Exercise: improve the Equal TC with an implicit conversion class
    ===(anotherValue: T)
    !==(anotherValue: T)
   */

  implicit class TypeSafeEqual[T](a: T) {
    def ===(b: T)(implicit equalizer: Equal[T]): Boolean = equalizer(a, b)

    def !==(b: T)(implicit equalizer: Equal[T]): Boolean = !equalizer(a, b)
  }

  println((john === anotherJohn))
  /*
    john.===(anotherJohn)
    new TypeSafeEqual[User}(john).===(anotherJohn)
    new TypeSafeEqual[User}(john).===(anotherJohn)(NameEquality)
   */

  /*
      TYPE SAFE
   */

  println(john == 43)
 // println(john === 43) // TYPE SAFE
}

package lectures.part4implicits

/**
  * Created by soner.guzeloglu onn 27.08.2019
  */
object OrganizingImplicits extends App {

  implicit def reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)

  // implicit val normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ < _)
  println(List(1, 4, 5, 3, 2).sorted)

  // scala.Predef

  /*
    Implicits ( used as implicit parameter)
        - val/var
        - objects
        - accessor methods = defs with no parantheses
   */


  // Exercise
  case class Person(name: String, age: Int)

  val persons = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 66),
  )

  /*object Person {
    implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }*/

  //  implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age < b.age) // AGE HAS THE PRIORITY HERE
  //  println(persons.sorted)

  /*
    Implicit Scope
        - normal scope = LOCAL SCOPE: where we write our code
        - imported scope = like EC.global
        - companion objects of all types involved in the method signature
            -List
            -Ordering
            all the types involved
   */

  //  def sorted[B >: A](implicit ord: Ordering[B]): Repr

  object AlphabeticNameOrdering {
    implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }

  object AgeOrdering {
    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age < b.age)
  }

  import AlphabeticNameOrdering._

  println(persons.sorted)


  /*
      Exercise
        - totalPrice = most used (50%)
        - by unit count = 25%
        - by unitPrice = 25%
   */

  case class Purchase(nUnits: Int, unitPrice: Double)

  object Purchase {
    implicit val totalOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.nUnits * a.unitPrice < b.nUnits * b.unitPrice)
  }

  object UnitsOrdering {
    implicit val nUnitsOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.nUnits < b.nUnits)
  }

  object PriceOrdering {
    implicit val nUnitsOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.unitPrice < b.unitPrice)
  }


}

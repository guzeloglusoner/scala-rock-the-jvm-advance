package lectures.part5ts

/**
  * Created by soner.guzeloglu onn 6.09.2019
  */
object Variance extends App {

  trait Animal

  class Dog extends Animal

  class Cat extends Animal

  class Crocodile extends Animal

  // what is variance?
  // Inheritance - type substitution of generics

  class Cage[T]

  // yes - covariance

  class CCage[+T]

  val ccage: CCage[Animal] = new CCage[Cat]

  // no - invariance

  class ICage[T]

  /*val icage: ICage[Animal] = new ICage[Cat]
  val x: Int = "hello"
  */

  // hell no - opposite = contravariance
  class XCage[-T]

  val xcage: XCage[Cat] = new XCage[Animal]

  class InvariantCage[T](val animal: T) // invariant

  // covariant positions
  class CovariantCage[+T](val animal: T) // covariant position

  //class ContravariantCage[-T](val animal: T)

  //val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)

  // class CovariantVariableCage[+T](var animal: T) // type of vars are in CONTRAVARIANT position

  /*
    val ccage: CCage[Animal] = new CCage[Cat](new Cat)
    ccage.animal = new Crocodile

   */

  // class ContravariantVariableCage[-T](var animal: T) // also in COVARIANT POSITION

  /*
*     val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)
*
* */

  class InvariantVariableCage[T](var animal: T) // ok

  /*trait AnotherCovariantCage[+T]{
    def addAnimal(animal: T)
  }*/
  /*
    val ccage: CCage[Animal] = new CCage[Dog]
    ccage.add(new Cat)

   */


  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true
  }

  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  acc.addAnimal(new Cat)

  class Kitty extends Cat

  acc.addAnimal(new Kitty)

  class MyList[+A] {
    def add[B >: A](elem: B): MyList[B] = new MyList[B] // widening the type
  }

  val emptyList = new MyList[Kitty]
  val animals =  emptyList.add(new Kitty)
  val moreAnimal = animals.add(new Cat)
  val evenMoreAnimals = moreAnimal.add(new Dog)

  // METHOD ARGUMENTS ARE IN CONTRAVARIANT POSITION

  // RETURN TYPES

  class PethShop[-T]{
//    def get(isItaPuppy: Boolean) : T // Method return types are in covariant position
    /*
      val catShop = new PetShop[Animal]{
        def get(isItaPuppy: Boolean) : Animal = new Cat
      }

      val dogShop: PetShop[Dog] = catShop
      dogShop.get(true) // EVIL CAT!
     */

    def get[S <: T](isItaPuppy: Boolean, defaultAnimal: S) = defaultAnimal
  }

  val shop: PethShop[Dog] = new PethShop[Animal]
//  val evilCat = shop.get(true,new Cat)
  class TerraNova extends Dog
  val bigFurry = shop.get(true, new TerraNova)

  /*
      BIG RULE

      - method arguments are in contravariant position
      - return types are in covariant position

   */

  /**
    * 1. Invariant, covariant, contravariant
    *
    * Parking[T](thins List[T}) {
    *     park(vehicle:T)
    *     impound(vehicle: List[T])
    *     checkVehicles(conditions: String): List[T]
    *
    *
    * }
    *
    * 2. used someone else's API: IList[T]
    *
    * 3. Parking = monad!
    *   - flatMap
    */

  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle
  class IList[T]

  class IParking[T](vehicle: List[T]){
    def park(vehicle: T): IParking[T] = ???
    def impound(vehicle: List[T]): IParking[T] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }

  class CParking[+T](vehicles: List[T]) {
    def park[S >: T](vehicle: S): CParking[S] = ???
    def impound[S >: T](vehicles: List[S]): CParking[S] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => CParking[S]): CParking[S] = ???
  }

  class XPark[-T](vehicles: List[T]){
    def park(vehicle: T): XPark[T] = ???
    def impound(vehicles: List[T]): XPark[T] = ???
    def checkVehicles[S <: T](conditions: String): List[S] = ???
    def flatMap[R <: T, S](f: R => XPark[S]): XPark[S] = ???
  }

  /*
    Rule of thumb

      - use covariance = COLLECTION OF THINGS
      - use cotravariance = GROUP OF ACTIONS
   */

  class CParking2[+T](vehicles: IList[T]) {
    def park[S >: T](vehicle: S): CParking2[S] = ???
    def impound[S >: T](vehicles: IList[S]): CParking2[S] = ???
    def checkVehicles[S >: T](conditions: String): IList[S] = ???
  }

  class XPark2[-T](vehicles: IList[T]){
    def park(vehicle: T): XPark2[T] = ???
    def impound[S <: T](vehicles: IList[S]): XPark2[S] = ???
    def checkVehicles[S <: T](conditions: String): IList[S] = ???
  }

  // 3 flatMap
}

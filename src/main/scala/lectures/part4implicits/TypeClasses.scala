package lectures.part4implicits

/**
  * Created by soner.guzeloglu onn 27.08.2019
  */
object TypeClasses extends App {

  trait HTMLWritable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHtml: String = s"<div>$name ($age yo) <a href$email/> </div>"
  }

  //User("John", 32,"john@rockthejvm.cm").toHtml
  val john = User("John", 32, "john@rockthejvm.cm")

  /*
      1- FOR THE TYPES we WRITE
      2- ONE implementation out of quite member
   */

  // option 2 - pattern matching
  object HTMLSerializerPM {
    def serializeToHtml(value: Any) = value match {
      case User(n, a, e) =>
      case _ =>
    }

    /*
        1- lost the type safety
        2- when new data structure comes we need to modify the code
        3- still ONE implementation
     */
  }

  // option 3

  // Type class
  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  // Type class instances
  implicit object UserSerializer extends HTMLSerializer[User] {
    def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href${user.email}/> </div>"
  }


  println(UserSerializer.serialize(john))

  // 1 - we can define serializers for other types
  import java.util.Date

  object DateSerializer extends HTMLSerializer[Date] {
    override def serialize(date: Date): String = s"<div>${date.toString()}</div>"

  }

  // 2 - we can define MULTIPLE serializers
  object PartialUserSerializer extends HTMLSerializer[User] {
    def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href${user.email}/> </div>"
  }

  // part 2
  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)

    def apply[T](implicit serializer: HTMLSerializer[T]) = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div style: color=blue>$value</div>"
  }


  println(HTMLSerializer.serialize(42))
  println(HTMLSerializer.serialize(john))

  // access to the entire type class interface
  println(HTMLSerializer[User].serialize(john))


  // part 3
  implicit class HTMLEnrichment[T](value: T) {
    def toHTML(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
  }

  println(john.toHTML) // println(new HTMLEnrichment[User](john).toHTML(UserSerializer))
  // COOL!

  /*
      - extend to any types
      - choose implementation (either by import or pass explicity)
      - super expressive!
   */

  println(2.toHTML)
  println(john.toHTML(PartialUserSerializer))

  /*
      - type class itself (trait) --------->HTMLSerializer[T}{ .. }
      - type class instances (some of which are implicit) --------->UserSerializer, IntSerializer
      - conversion with implicit classes --------->HTMLEnrichment
   */

  // context bounds
  def htmlBoilerplate[T](content: T)(implicit serializer: HTMLSerializer[T]): String =
    s"<html><body> ${content.toHTML(serializer)}</body></html>"

  /*Advantage is easy to use but you can not use explicit serializer */
  def htmlSugar[T: HTMLSerializer](content: T): String = {
    val serializer = implicitly[HTMLSerializer[T]]
    // user serializer
    s"<html><body> ${content.toHTML(serializer)}</body></html>"
  }

  // implicitly
  case class Permissions(mask: String)

  implicit val defaultPermissons: Permissions = Permissions("0744")

  // in some other part of the code
  val standardPerms = implicitly[Permissions]

}
package lectures.part3concurrency

import java.util.concurrent.Executors

/**
  * Created by soner.guzeloglu onn 25.08.2019
  */
object Intro extends App {

  /*
    interface Runnable {
      public void run()
    }
   */
  // JVM threads
  val runnable = new Runnable {
    override def run(): Unit = println("Running in parallel")
  }
  val aThread = new Thread(runnable)

  //  aThread.start() // gives the signal to the JVM to start a JVm thread
  //  // create a JVM thread => OS thread
  //  runnable.run() // does not do anything in parallel
  //  aThread.join() // block until aThread finishes running

  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
  val threadGoodBye = new Thread(() => (1 to 5).foreach(_ => println("goodbye")))
  //  threadHello.start()
  //  threadGoodBye.start()
  // different runs produce different results!

  // executors (re-use threads)
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("Something in the thread pool"))

  //  pool.execute(() => {
  //    Thread.sleep(1000)
  //    println("Done after 1 second")
  //  })
  //
  //  pool.execute(() => {
  //    Thread.sleep(1000)
  //    println("almost done")
  //    Thread.sleep(1000)
  //    println("done after 2 seconds")
  //  })

  pool.shutdown()
  // pool.execute(() => println("should not apper")) // throws an exception in the calling thread

  // pool.shutdownNow()
  println(pool.isShutdown) // true


  def runInParallel: Unit = {
    var x = 0

    val thread1 = new Thread(() => {
      x = 1
    })

    val thread2 = new Thread(() => {
      x = 2
    })

    thread1.start()
    thread2.start()

    println(x)
  }

  //  for (_ <- 1 to 10000) runInParallel

  // race condition

  class BankAccount(@volatile var amount: Int) {
    override def toString: String = "" + amount
  }

  def buy(account: BankAccount, thing: String, price: Int): Unit = {
    account.amount -= price
    //    println("I ve bought " + thing)
    //    println("My account is now " + account)
  }

  //  for (_ <- 1 to 1000) {
  //    val account = new BankAccount(50000)
  //    val thread1 = new Thread(() => buy(account, "shoes", 3000))
  //    val thread2 = new Thread(() => buy(account, "apple12", 4000))
  //
  //    thread1.start()
  //    thread2.start()
  //    Thread.sleep(100)
  //    if (account.amount != 43000) println("AHA: " + account.amount)
  //    //    println()
  //  }

  // option #1: use synchronized()
  def buySafe(account: BankAccount, thing: String, price: Int): Unit = {
    account.synchronized {
      account.amount -= price
      println("I ve bought " + thing)
      println("My account is now " + account)
    }
  }

  // option #2: use @volatile

  /*
    Exercises

    1) Construct 50 "inception" thread
        Thread1 -> Thread2 -> thread3 ...
        println("hello from thread #3"
        in REVERSE ORDER

   */
  def inceptionThreads(maxThreads: Int, i: Int = 1): Thread = new Thread(() => {
    if (i < maxThreads) {
      val newThread = inceptionThreads(maxThreads, i + 1)
      newThread.start()
      newThread.join()
    }
    println(s"hello from thread $i")
  })

  inceptionThreads(50).start()


  /*
    Exercise 2
   */
  var x = 0
  val threads = (1 to 100).map(_ => new Thread(() => x += 1))
  threads.foreach(_.start())

  /*
  1) what is the biggest value possible for x 100
  2) what is the smallest value possible for x 1

   */

  /*
    3 sleep fallacy
   */
  threads.foreach(_.join())
  println(x)
  var message = ""
  val awesomeThread = new Thread(() => {
    Thread.sleep(1000)
    message = "scala is awesome"
  })
  message = "scala sucks"
  awesomeThread.start()
  Thread.sleep(1000)
  awesomeThread.join() // waiti for the awesome thread to join
  println(message)
  /*
    whats the value of message?
    is it guaranteed?
    why? why not?

    Synchronized not work here.
   */


}

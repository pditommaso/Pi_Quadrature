/*
 *  Calculation of Pi using quadrature realized with a basic sequential algorithm using a while statement.
 *  Show the JVM warm up by executing the same thing a number of times.
 *
 *  Copyright © 2009–2012 Russel Winder
 */

object Pi_Scala_Sequential_WarmUp {
  def execute {
    val n = 1000000000
    //val n = 1000000000l
    val delta = 1.0 / n
    val startTimeNanos = System.nanoTime
    var sum = 0.0
    for ( i <- 1 to n ) {
    //for ( i <- 1l to n ) {
      val x = ( i - 0.5 ) * delta
      sum += 1.0 / ( 1.0 + x * x )
    }
    val pi = 4.0 * delta * sum
    val elapseTime = ( System.nanoTime - startTimeNanos ) / 1e9
    println ( "==== Scala Sequential WarmUp pi = " + pi )
    println ( "==== Scala Sequential WarmUp iteration count = " + n )
    println ( "==== Scala Sequential WarmUp elapse = " + elapseTime )
  }
  def main ( args : Array[String] ) {
    execute
    println
    execute
    println
    execute
    println
    execute
  }
}

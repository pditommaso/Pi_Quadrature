/*
 *  Calculation of Pi using quadrature realized with a fork/join approach with threads.
 *
 *  Copyright © 2009,2011 Russel Winder
 */
public class Pi_CS_ThreadsTaskClassDelegateSyncObject {
  private class Accumulator {
    private double sum = 0.0 ;
    public void add ( double value ) { lock ( this ) { sum += value ; } }
    public double getSum ( ) { return sum ; }
  }
  private class Task {
    private readonly long start ;
    private readonly long end ;
    private readonly double delta ;
    private readonly Accumulator accumulator ;
    public Task ( long start , long end , double delta , Accumulator accumulator ) {
      this.start = start ;
      this.end = end ;
      this.delta = delta ;
      this.accumulator = accumulator ;
    }
    public void execute ( ) {
      double localSum = 0.0 ;
      for ( long i = start ; i <= end ; ++i ) {
        double x = ( i - 0.5 ) * delta ;
        localSum += 1.0 / ( 1.0 + x * x ) ;
      }
      accumulator.add ( localSum ) ;
    }
  }
  private static void execute ( int numberOfTasks ) {
    const long n = 1000000000L ;
    const double delta = 1.0 / n ;
    long startTimeHundredsOfNanos = System.DateTime.Now.Ticks ;
    long sliceSize = n / numberOfTasks ;
    System.Threading.Thread[] threads = new System.Threading.Thread [ numberOfTasks ] ;
    Accumulator accumulator = new Accumulator ( ) ;
    for ( int i = 0 ; i < numberOfTasks ; ++i ) {
      Task task = new Task ( 1 + i * sliceSize ,  ( i + 1 ) * sliceSize , delta , accumulator ) ;
      threads[i] = new System.Threading.Thread ( new System.Threading.ThreadStart ( task.execute ) ) ;
    }
    foreach ( System.Threading.Thread t in threads ) { t.Start ( ) ; }
    foreach ( System.Threading.Thread t in threads ) { t.Join ( ) ; }
    double pi = 4.0 * delta * accumulator.getSum ( ) ;
    double elapseTime = ( System.DateTime.Now.Ticks - startTimeHundredsOfNanos ) / 1e7 ;
    System.Console.WriteLine ( "==== C# Threads Task Class Delegate Sync Object pi = " + pi ) ;
    System.Console.WriteLine ( "==== C# Threads Task Class Delegate Sync Object iteration count = " + n ) ;
    System.Console.WriteLine ( "==== C# Threads Task Class Delegate Sync Object elapse = " + elapseTime ) ;
    System.Console.WriteLine ( "==== C# Threads Task Class Delegate Sync Object processor count = " + System.Environment.ProcessorCount ) ;
    System.Console.WriteLine ( "==== C# Threads Task Class Delegate Sync Object thread count = " + numberOfTasks ) ;
  }
  public static void Main ( string[] args ) {
    Pi_CS_ThreadsTaskClassDelegateSyncObject.execute ( 1 ) ;
    System.Console.WriteLine ( ) ;
    Pi_CS_ThreadsTaskClassDelegateSyncObject.execute ( 2 ) ;
    System.Console.WriteLine ( ) ;
    Pi_CS_ThreadsTaskClassDelegateSyncObject.execute ( 8 ) ;
    System.Console.WriteLine ( ) ;
    Pi_CS_ThreadsTaskClassDelegateSyncObject.execute ( 32 ) ;
  }
}

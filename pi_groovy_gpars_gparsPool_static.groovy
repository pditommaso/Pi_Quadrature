#! /usr/bin/env groovy

/*
 *  Calculation of Pi using quadrature realized with a fork/join approach with GPars Parallelizer to
 *  partition the problem and hence harness all processors available to the JVM.
 *
 *  Copyright © 2010–2012 Russel Winder
 */

import groovyx.gpars.GParsPool

void execute ( final int numberOfTasks ) {
  GParsPool.withPool {
    final int n = 1000000000i
    final double delta = 1.0d / n
    final startTimeNanos = System.nanoTime ( )
    final int sliceSize = n / numberOfTasks
    final items = 0i ..< numberOfTasks
    final pi = 4.0d * delta * items.collectParallel { taskId ->
      PartialSum.compute ( taskId , sliceSize , delta )
    }.sumParallel ( )
    final elapseTime = ( System.nanoTime ( ) - startTimeNanos ) / 1e9
    Output.out ( getClass ( ).name , pi , n , elapseTime , numberOfTasks )
  }
}

execute 1
execute 2
execute 8
execute 32
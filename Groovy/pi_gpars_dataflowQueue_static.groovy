#! /usr/bin/env groovy

/*
 *  Caluclation of π using quadrature realized with GPars dataflow variables.
 *
 *  Copyright © 2011–2012 Russel Winder
 */

import groovyx.gpars.dataflow.DataflowQueue
import static groovyx.gpars.dataflow.Dataflow.task

void execute(final operatorCount) {
  final n = 1000000000
  final delta = 1.0 / n
  final startTimeNanos = System.nanoTime ()
  final sliceSize = (int)(n / operatorCount)
  final partialSums = new DataflowQueue()
  (0 ..< operatorCount).each {index ->
    task {partialSums << PartialSum.staticCompile(index, sliceSize, delta)}
  }
  final pi = 4.0 * delta * (0 ..< operatorCount).sum {partialSums.val}
  final elapseTime = (System.nanoTime() - startTimeNanos) / 1e9
  Output.out(getClass().name, pi, n, elapseTime, operatorCount)
}

execute 1
execute 2
execute 8
execute 32

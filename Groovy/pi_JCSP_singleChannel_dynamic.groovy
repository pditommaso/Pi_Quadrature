#! /usr/bin/env groovy

/*
 *  Calculation of π using quadrature realized with a fork/join approach with JCSP to partition the problem
 *  and hence harness all processors available to the JVM.
 *
 *  Copyright © 2010–2012 Russel Winder
 */

@Grab('org.codehaus.jcsp:jcsp:1.1-rc5')

import org.jcsp.lang.Channel
import org.jcsp.lang.CSProcess
import org.jcsp.lang.Parallel

void execute(int numberOfTasks) {
  final int n = 100000000i // 10 times fewer than Java due to speed issues.
  final double delta = 1.0d / n
  final startTimeNanos = System.nanoTime()
  final int sliceSize = n / numberOfTasks
  final channel = Channel.any2one()
  final processes = (0i ..< numberOfTasks).collect { taskId ->
    { ->
      final int start = 1i + taskId * sliceSize
      final int end = (taskId + 1i) * sliceSize
      double sum = 0.0
      for (int j in start .. end) {
        final double x = (j - 0.5d) * delta
        sum += 1.0d / (1.0d + x * x)
      }
      channel.out().write(sum)
    } as CSProcess
  }
  processes << {
      final double pi = 4.0d * delta *(0i ..< numberOfTasks).sum{(double)channel.in().read()}
      final elapseTime = (System.nanoTime() - startTimeNanos) / 1e9
      Output.out(getClass().name, pi, n, elapseTime, numberOfTasks)
    } as CSProcess
  new Parallel(processes as CSProcess[]).run()
}

execute 1
execute 2
execute 8
execute 32

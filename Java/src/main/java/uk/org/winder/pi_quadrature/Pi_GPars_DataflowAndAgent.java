/*
 *  Calculation of π using quadrature realized with GPars data flow tasks and an agent.
 *
 *  Copyright © 2010–2013  Russel Winder
 */

package uk.org.winder.pi_quadrature;

/*
 *  This program provided by Václav Pech by private email.  It is based on
 *  Pi_Java_ThreadsSynchronizedStatement amended to use GPars data flow variables and agents.
 */

import groovyx.gpars.MessagingRunnable;
import groovyx.gpars.agent.Agent;
import groovyx.gpars.dataflow.Dataflow;
import groovyx.gpars.dataflow.Promise;

public class Pi_GPars_DataflowAndAgent {

  private static class Accumulator {
    private double sum = 0.0;
    void add(final double value) { sum += value; }
    public double getSum() { return sum; }
  }

  private static void execute(final int numberOfTasks) {
    final int n = 1000000000;
    final double delta = 1.0 / n;
    final long startTimeNanos = System.nanoTime();
    final int sliceSize = n / numberOfTasks;
    final Promise<?>[] tasks = new Promise[numberOfTasks];
    final Agent<Accumulator> sum = new Agent<>(new Accumulator());
    for (int i = 0; i < numberOfTasks; ++i) {
      final int taskId = i;
      tasks[taskId] = Dataflow.task(new Runnable() {
          @Override public void run() {
            final int start = 1 + taskId * sliceSize;
            final int end = (taskId + 1) * sliceSize;
            double localSum = 0.0;
            for (int i = start; i <= end; ++i) {
              final double x = (i - 0.5d) * delta;
              localSum += 1.0 / (1.0 + x * x);
            }
            final double currentSum = localSum;
            sum.send(new MessagingRunnable<Accumulator>() {
                private static final long serialVersionUID = 1L;
                @Override protected void doRun(final Accumulator t) {
                  t.add(currentSum);
                }
              });
          }
        });
    }
    for (final Promise<?> t : tasks) {
      try { t.join(); }
      catch (final InterruptedException ie) { throw new RuntimeException(ie); }
    }
    try {
      final double pi = 4.0 * delta * sum.getVal().getSum();
      final double elapseTime = (System.nanoTime() - startTimeNanos) / 1e9;
      Output.out( "Pi_GPars_DataflowAndAgent", pi, n, elapseTime, numberOfTasks);
    }
    catch (InterruptedException ie) { throw new RuntimeException(ie); }
  }

  public static void main(final String[] args) {
    Pi_GPars_DataflowAndAgent.execute(1);
    Pi_GPars_DataflowAndAgent.execute(2);
    Pi_GPars_DataflowAndAgent.execute(8);
    Pi_GPars_DataflowAndAgent.execute(32);
  }
}

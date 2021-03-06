/*
 *  Calculation of π using quadrature realized with a fork/join approach with threads.
 *
 *  Copyright © 2009–2013  Russel Winder
 */

package uk.org.winder.pi_quadrature;

public class Pi_ThreadsAccumulatorClass {

  private static class Accumulator {
    private double sum = 0.0;
    public synchronized void add(final double value) { sum += value; }
    public synchronized double getSum() { return sum; }
  }

  private static void execute(final int numberOfTasks) {
    final int n = 1000000000;
    final double delta = 1.0 / n;
    final long startTimeNanos = System.nanoTime();
    final int sliceSize = n / numberOfTasks;
    final Thread[] threads = new Thread[numberOfTasks];
    final Accumulator accumulator = new Accumulator();
    for (int i = 0; i < numberOfTasks; ++i) {
      final int taskId = i;
      threads[taskId] = new Thread(new Runnable() {
          @Override public void run() {
            final int start = 1 + taskId * sliceSize;
            final int end = (taskId + 1) * sliceSize;
            double sum = 0.0;
            for (int i = start; i <= end; ++i) {
              final double x = (i - 0.5) * delta;
              sum += 1.0 / (1.0 + x * x);
            }
            accumulator.add(sum);
          }
        });
    }
    for (final Thread t : threads) { t.start(); }
    for (final Thread t : threads) {
      try { t.join(); }
      catch (final InterruptedException ie) { throw new RuntimeException("Got an InterruptedException joining a thread.", ie); }
    }
    final double pi = 4.0 * delta * accumulator.getSum();
    final double elapseTime = (System.nanoTime() - startTimeNanos) / 1e9;
    Output.out("Pi_ThreadsAccumulator Class", pi, n, elapseTime, numberOfTasks);
  }

  public static void main(final String[] args) {
    Pi_ThreadsAccumulatorClass.execute(1);
    Pi_ThreadsAccumulatorClass.execute(2);
    Pi_ThreadsAccumulatorClass.execute(8);
    Pi_ThreadsAccumulatorClass.execute(32);
  }
}

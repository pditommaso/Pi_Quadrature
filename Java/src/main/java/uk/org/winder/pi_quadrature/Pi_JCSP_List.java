/*
 *  Calculation of π using quadrature realized with a fork/join approach with JCSP to partition the problem
 *  and hence harness all processors available to the JVM.
 *
 *  Copyright © 2009–2013  Russel Winder
 */

package uk.org.winder.pi_quadrature;

import org.jcsp.lang.Channel;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannel;
import org.jcsp.lang.Parallel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pi_JCSP_List {

  private static void execute(final int numberOfTasks) {
    final int n = 1000000000;
    final double delta = 1.0 / n;
    final long startTimeNanos = System.nanoTime();
    final int sliceSize = n / numberOfTasks;
    //
    //  Cannot just do:
    //
    //    final List<One2OneChannel<Object>> channels = Arrays.asList(Channel.one2oneArray(numberOfTasks));
    //
    //  as this gets the wrong version of the type-parameterized one2oneArray method.
    //
    final One2OneChannel<Double>[] backing = Channel.one2oneArray(numberOfTasks);
    final List<One2OneChannel<Double>> channels = Arrays.asList(backing);
    final List<CSProcess> processes = new ArrayList<CSProcess>();
    for (int i = 0; i < numberOfTasks; ++i) {
      final int taskId = i;
      processes.add(new CSProcess() {
          @Override public void run() {
            final int start = 1 + taskId * sliceSize;
            final int end = (taskId + 1) * sliceSize;
            double sum = 0.0;
            for (int i = start; i <= end; ++i) {
              final double x = (i - 0.5) * delta;
              sum += 1.0 / (1.0 + x * x);
            }
            channels.get(taskId).out().write(sum);
          }
        });
    }
    processes.add(new CSProcess() {
        @Override public void run() {
          double sum = 0.0;
          for (final One2OneChannel<Double> c : channels) { sum += c.in().read(); }
          final double pi = 4.0 * delta * sum;
          final double elapseTime = (System.nanoTime() - startTimeNanos) / 1e9;
          Output.out("Pi_JCSP_List", pi, n, elapseTime, numberOfTasks);
        }
      });
    new Parallel(processes.toArray(new CSProcess[0])).run();
  }

  public static void main(final String[] args) {
    Pi_JCSP_List.execute(1);
    Pi_JCSP_List.execute(2);
    Pi_JCSP_List.execute(8);
    Pi_JCSP_List.execute(32);
  }
}

/*
 *  A Chapel program to calculate Pi using quadrature as a sequential algorithm.
 *
 *  Copyright © 2009, 2011–13  Russel Winder
 */

use Time;
use Output;

proc main() {
  param n:int(64) = 1000000000;
  const delta:real = 1.0 / n;
  var timer:Timer;
  timer.start();
  var sum:real = 0.0;
  var i:int(64) = 0;
  while (i < n) {
    sum += 1.0 / (1.0 + ((i - 0.5) * delta) ** 2);
    i += 1;
  }
  const pi = 4.0 * delta * sum;
  timer.stop();
  output("Chapel Sequential While Power", pi, n, timer.elapsed());
}

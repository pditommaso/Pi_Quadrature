/*
 *  A D program to calculate π using quadrature as a sequential reduce of individual expression evaluations
 *  with no manual batching. This is really just here as a comparison against the parallel version.
 *
 *  Copyright © 2011–2013  Russel Winder
 */

import std.algorithm;
import std.datetime;
import std.range;

import outputFunctions;

int main(immutable string[] args) {
  immutable n = 1000000000;
  immutable delta = 1.0 / n;
  StopWatch stopWatch;
  stopWatch.start();
  immutable pi = 4.0 * delta * reduce!((a, b) => a + b)(
       map!((int i){ immutable x = (i - 0.5) * delta; return 1.0 / (1.0 + x * x); })(iota(n)));
  stopWatch.stop();
  immutable elapseTime = stopWatch.peek().hnsecs * 100e-9;
  output(__FILE__, pi, n, elapseTime);
  return 0;
}

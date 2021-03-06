#! /usr/bin/env groovy

/*
 *  Calculation of π using quadrature realized with GPars actors. Scripty.  With Java computation.
 *
 *  Copyright © 2010–2012 Russel Winder.
 */

//  This variant evolved from one provided by Václav Pech by private email.

import groovyx.gpars.actor.Actors
import groovyx.gpars.actor.DefaultActor

final class AccumulatorActor extends DefaultActor {
  private final actorCount
  def sum
  def AccumulatorActor(actorCount) { this.actorCount = actorCount }
  @Override protected void act() { handleMessage(0, actorCount, 0.0) }
  //  There is no recursive function call here — it is more like a tail recursive call or a continuation.
  //  The call of handleMessage happens inside the react scope and so registers a new message handler with
  //  the state captured by the parameters.  It definitely does not create a new function call with a new
  //  stack frame.
  void handleMessage(final n, final max, final sum) {
    if (n < max) {
      react{handleMessage(n + 1, max, sum + it)}
    }
    else {
      this.sum = sum
      terminate()
    }
  }
}

void execute(final actorCount) {
  final n = 1000000000
  final delta = 1.0 / n
  final startTimeNanos = System.nanoTime()
  final sliceSize = (int)(n / actorCount)
  final accumulator = new AccumulatorActor(actorCount)
  accumulator.start()
  (0 ..< actorCount).each {id ->
    Actors.actor {accumulator << new ProcessSlice(id, sliceSize, delta).compute()}
  }
  accumulator.join()
  final pi = 4.0 * delta * accumulator.sum
  final elapseTime = (System.nanoTime() - startTimeNanos) / 1e9
  Output.out(getClass().name, pi, n, elapseTime, actorCount)
}

execute 1
execute 2
execute 8
execute 32

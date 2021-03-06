/*
 *  Sequential implementation of π by quadrature using imperative approach.
 *
 *  Copyright © 2013, 2014  Russel Winder
 */

extern crate time;
extern crate output;

use time::precise_time_s;
use output::output;

fn main() {
    let n = 1000000000u;
    let delta = 1.0 / n as f64;
    let startTime = precise_time_s();
    let mut sum = 0.0;
    let mut i = 0;
    while i < n {
        let x = (i as f64 - 0.5) * delta;
        sum += 1.0 / (1.0 + x * x);
        i += 1
    }
    let pi = 4.0 * delta * sum;
    let elapseTime = precise_time_s() - startTime;
    output("pi_sequential_while", pi, n, elapseTime)
}

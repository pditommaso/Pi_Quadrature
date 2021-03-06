/*
 *  Output functions for the Rust realizations of  π by quadrature.
 *
 *  Copyright © 2013, 2014  Russel Winder
 */

#[crate_id = "output#0.0"];

pub fn output(banner:&str, pi:f64, n:uint, elapseTime:f64) {
    println!("======================== {}", banner);
    println!("\tπ = {:.18f}", pi);
    println!("\titeration count = {}", n);
    println!("\telapse time = {}", elapseTime);
}

pub fn outputN(banner:&str, pi:f64, n:uint, elapseTime:f64, numberOfTasks:uint) {
    output(format!("{}, task count: {}", banner, numberOfTasks), pi, n, elapseTime);
    println!("\tnumber of processors = {}", 8);
}

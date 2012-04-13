#! /usr/bin/env python3
# -*- mode:python; coding:utf-8; -*-

#  Calculation of Pi using quadrature. Sequential algorithm.  Use reduce.
#
#  Copyright © 2008–2011 Russel Winder

import time
from functools import reduce

def f ( s , i ) :
    x = ( i - 0.5 ) * delta
    return s + 1.0 / ( 1.0 + x * x )

if __name__ == '__main__' :
    n = 10000000 # 100 times fewer due to speed issues.
    delta = 1.0 / n
    startTime = time.time ( )
    pi = 4.0 * delta * reduce ( f , range ( n ) , 0.0 )
    elapseTime = time.time ( ) - startTime
    print ( "==== Python Sequential Reduce Multiply pi = " + str ( pi ) )
    print ( "==== Python Sequential Reduce Multiply iteration count = " + str ( n ) )
    print ( "==== Python Sequential Reduce Multiply elapse = " + str ( elapseTime ) )
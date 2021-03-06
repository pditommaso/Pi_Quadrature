#! /usr/bin/env python
# -*- coding:utf-8; -*-

#  Calculation of π using quadrature. Sequential algorithm using a for loop with an xrange – xrange only
#  exists in Python 2, in Python 3 range is what xrange used to be in Python 2.
#
#  Copyright © 2008–2012 Russel Winder

from output import out
from time import time

if __name__ == '__main__':
    n = 10000000  # 100 times fewer than C due to speed issues.
    delta = 1.0 / n
    startTime = time()
    sum = 0.0
    for i in xrange(1, n + 1):
        x = (i - 0.5) * delta
        sum += 1.0 / (1.0 + x * x)
    pi = 4.0 * delta * sum
    elapseTime = time() - startTime
    out(__file__, pi, n, elapseTime)

#! /usr/bin/env python
# -*- coding:utf-8; -*-

#  Calculation of π using quadrature. Sequential algorithm using NumPy fromfunction.
#
#  Copyright © 2008–2013  Russel Winder

from numpy import double, fromfunction
from output import out
from time import time

if __name__ == '__main__':
    n = 100000000  # 10 times fewer than C due to speed issues.
    delta = 1.0 / n
    startTime = time()
    pi = 4.0 * delta * fromfunction(lambda i: 1.0 / (1.0 + ((i - 0.5) * delta) ** 2), (n,), dtype=double).sum()
    elapseTime = time() - startTime
    out(__file__, pi, n, elapseTime)

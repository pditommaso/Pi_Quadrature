#! /usr/bin/env python3

#  Calculation of π using quadrature. Uses threads and an extension.  ctypes is careful to release the GIL
#  whilst D code is running so we get real parallelism.
#
#  Copyright © 2008–2014  Russel Winder

from output import out
from queue import Queue
from threading import Thread
from time import time

import ctypes

def processSlice(id, sliceSize, delta, results):
    results.put(processSliceModule.processSlice(id, sliceSize, delta))

def execute(threadCount):
    n = 1000000000
    delta = 1.0 / n
    startTime = time()
    sliceSize = n // threadCount
    results = Queue(threadCount)
    threads = [Thread(target=processSlice, args=(i, sliceSize, delta, results)) for i in range(0, threadCount)]
    for thread in threads:
        thread.start()
    pi = 4.0 * delta * sum(results.get() for i in range(threadCount))
    elapseTime = time() - startTime
    out(__file__, pi, n, elapseTime, threadCount)

if __name__ == '__main__':
    processSliceModule = ctypes.cdll.LoadLibrary('processSlice_d.so')
    processSliceModule.processSlice.argtypes = [ctypes.c_int, ctypes.c_int, ctypes.c_double]
    processSliceModule.processSlice.restype = ctypes.c_double
    execute(1)
    execute(2)
    execute(8)
    execute(32)

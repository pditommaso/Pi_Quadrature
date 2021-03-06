#! /usr/bin/env python3

#  Calculation of π using quadrature. Uses threads and an extension.  CFFI is careful to release the GIL
#  whilst C code is running so we get real parallelism.
#
#  Copyright © 2008–2013 Russel Winder

from output import out
from queue import Queue
from threading import Thread
from time import time

from cffi import FFI

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
    ffi = FFI()
    ffi.cdef('double processSlice(int, int, double);')
    with open('processSlice_c.c', 'r') as file:
        processSliceModule = ffi.verify(file.read())
    execute(1)
    execute(2)
    execute(8)
    execute(32)

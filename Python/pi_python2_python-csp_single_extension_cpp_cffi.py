#! /usr/bin/env python
# -*- coding:utf-8; -*-

#  Calculation of π using quadrature. Usies the python-csp package by Sarah Mount.
#
#  Copyright © 2010–2013 Russel Winder

from csp.os_process import process, Channel, Par
from output import out
from time import time

from cffi import FFI

@process
def calculator(channel, id, sliceSize, delta):
    channel.write(processSliceModule.processSlice(id, sliceSize, delta))

@process
def accumulator(channel, n, delta, startTime, processCount):
    pi = 4.0 * delta * sum(channel.read() for i in xrange(0, processCount))
    elapseTime = time() - startTime
    out(__file__, pi, n, elapseTime, processCount)

def execute(processCount):
    n = 1000000000
    delta = 1.0 / n
    startTime = time()
    sliceSize = n / processCount
    channel = Channel()
    processes = []
    for i in xrange(0, processCount):
        processes.append(calculator(channel, i, sliceSize, delta))
    processes.append(accumulator(channel, n, delta, startTime, processCount))
    Par(*processes).start()

if __name__ == '__main__':
    ffi = FFI()
    ffi.cdef('double processSlice(int, int, double);')
    processSliceModule = ffi.dlopen('processSlice_cpp.so')
    execute(1)
    execute(2)
    execute(8)
    execute(32)

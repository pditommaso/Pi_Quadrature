#! /usr/bin/env python3

#  Calculation of π using quadrature. Uses the python-csp package by Sarah Mount.
#
#  Copyright © 2010–2013 Russel Winder

from csp.os_process import process, Channel, Par
from output import out
from time import time

from processSlice_cython_py3 import processSlice

@process
def calculator(channel, id, sliceSize, delta):
    channel.write(processSlice(id, sliceSize, delta))

@process
def accumulator(channel, n, delta, startTime, processCount):
    pi = 4.0 * delta * sum(channel.read() for i in range(0, processCount))
    elapseTime = time() - startTime
    out(__file__, pi, n, elapseTime, processCount)

def execute(processCount):
    n = 1000000000
    delta = 1.0 / n
    startTime = time()
    sliceSize = n // processCount
    channel = Channel()
    processes = []
    for i in range(0, processCount):
        processes.append(calculator(channel, i, sliceSize, delta))
    processes.append(accumulator(channel, n, delta, startTime, processCount))
    Par(*processes).start()

if __name__ == '__main__':
    execute(1)
    execute(2)
    execute(8)
    execute(32)

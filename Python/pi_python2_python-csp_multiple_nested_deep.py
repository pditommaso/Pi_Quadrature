#! /usr/bin/env python
# -*- coding:utf-8; -*-

#  Calculation of π using quadrature. Uses the python-csp package by Sarah Mount.
#
#  Copyright © 2009–2013 Russel Winder

from csp.os_process import process, Channel, Par
from output import out
from time import time

def execute(processCount):
    n = 10000000  # 100 times fewer than C due to speed issues.
    delta = 1.0 / n
    startTime = time()
    sliceSize = n / processCount
    channels = []

    @process
    def accumulator():
        pi = 4.0 * delta * sum(channel.read() for channel in channels)
        elapseTime = time() - startTime
        out(__file__, pi, n, elapseTime, processCount)

    processes = []
    for i in xrange(0, processCount):
        channel = Channel()
        channels.append(channel)

        @process
        def calculator(id):
            sum = 0.0
            for i in xrange(1 + id * sliceSize, (id + 1) * sliceSize):
                x = (i - 0.5) * delta
                sum += 1.0 / (1.0 + x * x)
            channels[id].write(sum)

        processes.append(calculator(i))
    processes.append(accumulator())
    Par(*processes).start()

if __name__ == '__main__':
    execute(1)
    execute(2)
    execute(8)
    execute(32)

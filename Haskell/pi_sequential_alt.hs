--  Haskell implementation of π by Quadrature
--
--  Copyright © 2009–2011, 2013  Russel Winder

module Main where

import Output (out)

piIter :: Int -> Double -> Double -> Double
piIter 0 delta accumulator = 4.0 * delta * accumulator
piIter n delta accumulator =
    let
        nMinus1 = n -1
        x = ((fromIntegral n) - 0.5) * delta
        value = accumulator + 1.0 / (1.0 + x * x)
    in
      piIter nMinus1 delta value

piQuad :: Int -> Double
piQuad n = piIter n (1.0 / (fromIntegral n)) 0.0

main :: IO()
main = do
  let n = 1000000000
  out "Sequential Alt" (piQuad n) n

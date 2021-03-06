--  Haskell implementation of π by Quadrature
--
--  Copyright © 2009–2011, 2013  Russel Winder

module Main where

import Output (out)

piIter :: Int -> Double -> Double -> Double
piIter 0 delta accumulator = 4.0 * delta * accumulator
piIter n delta accumulator = piIter (n - 1) delta (accumulator + 1.0 / (1.0 + x * x))
    where
      x = ((fromIntegral n) - 0.5) * delta

piQuad :: Int -> Double
piQuad n = piIter n (1.0 / (fromIntegral n)) 0.0

main :: IO()
main = do
  let n = 1000000000
  out "Sequential" (piQuad n) n

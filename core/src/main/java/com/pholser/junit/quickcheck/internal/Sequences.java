/*
 The MIT License

 Copyright (c) 2010-2016 Paul R. Holser, Jr.

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.pholser.junit.quickcheck.internal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.math.RoundingMode.*;

public final class Sequences {
    private Sequences() {
        throw new UnsupportedOperationException();
    }

    public static Iterable<BigInteger> halvingIntegral(BigInteger max, BigInteger start) {
        return () -> new Iterator<BigInteger>() {
            private boolean done;
            private BigInteger next = start;

            @Override public boolean hasNext() {
                return !done;
            }

            @Override public BigInteger next() {
                if (!hasNext())
                    throw new NoSuchElementException();

                next = peek();
                done = next.equals(peek());
                return next;
            }

            private BigInteger peek() {
                return next.add(max.subtract(next).divide(BigInteger.valueOf(2)));
            }
        };
    }

    public static Iterable<BigDecimal> halvingDecimal(BigDecimal max, BigDecimal start) {
        return () -> new Iterator<BigDecimal>() {
            private boolean done;
            private BigDecimal next = start;

            @Override public boolean hasNext() {
                return !done;
            }

            @Override public BigDecimal next() {
                if (!hasNext())
                    throw new NoSuchElementException();

                next = peek();
                done = next.equals(peek());
                return next;
            }

            private BigDecimal peek() {
                return next.add(max.subtract(next).divide(BigDecimal.valueOf(2), HALF_UP));
            }
        };
    }

    public static Iterable<Integer> halving(int start) {
        return () -> new Iterator<Integer>() {
            private boolean done;
            private int next = start;

            @Override public boolean hasNext() {
                return !done;
            }

            @Override public Integer next() {
                if (!hasNext())
                    throw new NoSuchElementException();

                int result = next;
                next = peek();
                done = next == 0;
                return result;
            }

            private int peek() {
                return next / 2;
            }
        };
    }
}

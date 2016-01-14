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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.google.common.collect.Lists.*;
import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.*;

public class SequencesTest {
    @Rule public final ExpectedException thrown = none();

    @Test public void halvingBigIntegers() {
        assertEquals(
            newArrayList(
                BigInteger.valueOf(5),
                BigInteger.valueOf(7),
                BigInteger.valueOf(8),
                BigInteger.valueOf(9)),
            newArrayList(Sequences.halvingIntegral(BigInteger.TEN, BigInteger.ZERO)));
    }

    @Test public void halvingBigDecimals() {
        assertEquals(
            newArrayList(
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(8),
                BigDecimal.valueOf(9),
                BigDecimal.TEN),
            newArrayList(Sequences.halvingDecimal(BigDecimal.TEN, BigDecimal.ZERO)));
    }

    @Test public void halvingInts() {
        assertEquals(
            newArrayList(27, 13, 6, 3, 1),
            newArrayList(Sequences.halving(27)));
    }

    @Test public void callingNextOutOfSequenceOnHalvingBigIntegers() {
        Iterator<BigInteger> i =
            Sequences.halvingIntegral(BigInteger.ZERO, BigInteger.ZERO).iterator();
        i.next();

        thrown.expect(NoSuchElementException.class);

        i.next();
    }

    @Test public void callingNextOutOfSequenceOnHalvingBigDecimals() {
        Iterator<BigDecimal> i =
            Sequences.halvingDecimal(BigDecimal.ZERO, BigDecimal.ZERO).iterator();
        i.next();

        thrown.expect(NoSuchElementException.class);

        i.next();
    }

    @Test public void callingNextOutOfSequenceOnHalvingInts() {
        Iterator<Integer> i = Sequences.halving(0).iterator();
        i.next();

        thrown.expect(NoSuchElementException.class);

        i.next();
    }
}

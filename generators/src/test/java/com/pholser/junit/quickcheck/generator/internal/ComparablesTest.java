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

package com.pholser.junit.quickcheck.generator.internal;

import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ComparablesTest {
    private Predicate<Integer> inRangeUnbounded;
    private Predicate<Integer> inRangeMinOnly;
    private Predicate<Integer> inRangeMaxOnly;
    private Predicate<Integer> inRangeBounded;

    @Before
    public void setUp() {
        inRangeUnbounded = Comparables.inRange(null, null);
        inRangeMinOnly = Comparables.inRange(-3, null);
        inRangeMaxOnly = Comparables.inRange(null, 6);
        inRangeBounded = Comparables.inRange(2, 3);
    }

    @Test public void inRangeUnbounded() {
        assertTrue(inRangeUnbounded.test(2));
    }

    @Test public void inRangeMinOnlyBelow() {
        assertFalse(inRangeMinOnly.test(-4));
    }

    @Test public void inRangeMinOnlyOnBound() {
        assertTrue(inRangeMinOnly.test(-3));
    }

    @Test public void inRangeMinOnlyAbove() {
        assertTrue(inRangeMinOnly.test(-2));
    }

    @Test public void inRangeMaxOnlyBelow() {
        assertTrue(inRangeMaxOnly.test(5));
    }

    @Test public void inRangeMaxOnlyOnBound() {
        assertTrue(inRangeMaxOnly.test(6));
    }

    @Test public void inRangeMaxOnlyAbove() {
        assertFalse(inRangeMaxOnly.test(7));
    }

    @Test public void inRangeBoundedBelowMin() {
        assertFalse(inRangeBounded.test(1));
    }

    @Test public void inRangeBoundedOnMin() {
        assertTrue(inRangeBounded.test(2));
    }

    @Test public void inRangeBoundedOnMax() {
        assertTrue(inRangeBounded.test(3));
    }

    @Test public void inRangeBoundedAbove() {
        assertFalse(inRangeBounded.test(4));
    }

    @Test public void leastMagnitudeUnbounded() {
        assertEquals(Integer.valueOf(0), Comparables.leastMagnitude(null, null, 0));
    }

    @Test public void leastMagnitudeNegativeMinOnly() {
        assertEquals(Integer.valueOf(0), Comparables.leastMagnitude(-3, null, 0));
    }

    @Test public void leastMagnitudePositiveMinOnly() {
        assertEquals(Integer.valueOf(4), Comparables.leastMagnitude(4, null, 0));
    }

    @Test public void leastMagnitudeNegativeMaxOnly() {
        assertEquals(Integer.valueOf(-2), Comparables.leastMagnitude(null, -2, 0));
    }

    @Test public void leastMagnitudePositiveMaxOnly() {
        assertEquals(Integer.valueOf(0), Comparables.leastMagnitude(null, 5, 0));
    }

    @Test public void leastMagnitudeBothLessThanZero() {
        assertEquals(Integer.valueOf(-1), Comparables.leastMagnitude(-4, -1, 0));
    }

    @Test public void leastMagnitudeBothGreaterThanZero() {
        assertEquals(Integer.valueOf(5), Comparables.leastMagnitude(5, 7, 0));
    }

    @Test public void leastMagnitudeStraddlingZero() {
        assertEquals(Integer.valueOf(0), Comparables.leastMagnitude(-2, 4, 0));
    }
}

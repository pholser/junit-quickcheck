/*
 The MIT License

 Copyright (c) 2004-2016 Paul R. Holser, Jr.

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

import java.util.Random;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.Test;

import static com.pholser.junit.quickcheck.internal.Ranges.*;
import static org.junit.Assert.*;

public class RangesTest {
    @Test public void checkFindNextPowerOfTwoLong() {
        assertEquals(1, findNextPowerOfTwoLong(1));
        assertEquals(2, findNextPowerOfTwoLong(2));
        assertEquals(4, findNextPowerOfTwoLong(3));
        assertEquals(4, findNextPowerOfTwoLong(4));
        assertEquals(8, findNextPowerOfTwoLong(5));
        assertEquals((long) 1 << 61, findNextPowerOfTwoLong((long) 1 << 61));
        assertEquals((long) 1 << 62, findNextPowerOfTwoLong(1 + (long) 1 << 61));
    }

    @Test public void chooseLongsMustReturnValuesInTheExpectedRange() {
        assertRangeOfRandomLong(-10L, 100L);
        assertRangeOfRandomLong(1L, 1L);
        assertRangeOfRandomLong(Long.MIN_VALUE, Long.MIN_VALUE + 1);
        assertRangeOfRandomLong(Long.MAX_VALUE - 1, Long.MAX_VALUE);
        assertRangeOfRandomLong(Long.MIN_VALUE, Long.MAX_VALUE);
    }

    @Test public void weakSanityCheckForDistributionOfChooseLongs() {
        boolean[] hits = new boolean[5];
        SourceOfRandomness random = new SourceOfRandomness(new Random(0));

        for (int i = 0; i < 100; i++) {
            hits[(int) Ranges.choose(random, 0, (long) hits.length - 1)] = true;
        }
        for (boolean hit : hits) {
            assertTrue(hit);
        }
    }

    private void assertRangeOfRandomLong(long min, long max) {
        SourceOfRandomness random = new SourceOfRandomness(new Random(0));
        for (int i = 0; i < 1000; i++) {
            long result = Ranges.choose(random, min, max);
            assertTrue(min <= result && result <= max);
        }
    }
}

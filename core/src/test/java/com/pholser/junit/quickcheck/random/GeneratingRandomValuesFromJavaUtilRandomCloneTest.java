/*
 The MIT License

 Copyright (c) 2010-2017 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.random;

import java.util.Random;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.runners.MethodSorters.*;

@FixMethodOrder(NAME_ASCENDING)
public class GeneratingRandomValuesFromJavaUtilRandomCloneTest {
    private SourceOfRandomness source;
    private Random clone;

    @Before public void beforeEach() {
        source = new SourceOfRandomness(new Random());
        source.setSeed(1L);

        // mix in some randomness operations so the internal state changes
        source.nextBoolean();
        source.nextBytes(4);
        source.nextDouble();

        clone = source.toJDKRandom();
    }

    @Test public void delegateNextBoolean() {
        assertEquals(source.nextBoolean(), clone.nextBoolean());
    }

    @Test public void delegateNextBytes() {
        byte[] sourceBytes = new byte[128];
        byte[] cloneBytes = new byte[128];

        source.nextBytes(sourceBytes);
        clone.nextBytes(cloneBytes);

        assertArrayEquals(sourceBytes, cloneBytes);
    }

    @Test public void delegateNextDouble() {
        assertEquals(source.nextDouble(), clone.nextDouble(), 0D);
    }

    @Test public void delegateNextFloat() {
        assertEquals(source.nextFloat(), clone.nextFloat(), 0F);
    }

    @Test public void delegateNextGaussian() {
        assertEquals(source.nextGaussian(), clone.nextGaussian(), 0F);
    }

    @Test public void delegateNextInt() {
        assertEquals(source.nextInt(), clone.nextInt());
    }

    @Test public void delegateNextIntFromZeroToN() {
        assertEquals(source.nextInt(2), clone.nextInt(2));
    }

    @Test public void delegatesNextLong() {
        assertEquals(source.nextLong(), clone.nextLong());
    }
}

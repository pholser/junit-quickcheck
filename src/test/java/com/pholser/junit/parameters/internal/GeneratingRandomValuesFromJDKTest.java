/*
 The MIT License

 Copyright (c) 2010-2011 Paul R. Holser, Jr.

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

package com.pholser.junit.parameters.internal;

import java.util.Random;

import static java.lang.Double.*;
import static java.lang.Float.*;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GeneratingRandomValuesFromJDKTest {
    private Random random;
    private JDKSourceOfRandomness source;

    @Before
    public void setUp() {
        random = mock(Random.class);
        source = new JDKSourceOfRandomness(random);
    }

    @Test
    public void fetchesNextIntFromAJavaUtilRandom() {
        source.nextInt();

        verify(random).nextInt();
    }

    @Test(expected = IllegalArgumentException.class)
    public void nextIntWithBackwardsRange() {
        source.nextInt(0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nextIntWithIdenticalMinAndMax() {
        source.nextInt(-2, -2);
    }

    @Test
    public void nextIntInRange() {
        int value = source.nextInt(3, 5);

        verify(random).nextInt(5 - 3 + 1);
        assertThat(value, lessThanOrEqualTo(5));
        assertThat(value, greaterThanOrEqualTo(3));
    }

    @Test
    public void nextBoolean() {
        source.nextBoolean();

        verify(random).nextBoolean();
    }

    @Test
    public void nextLong() {
        source.nextLong();

        verify(random).nextLong();
    }

    @Test
    public void nextFloat() {
        when(random.nextInt()).thenReturn(floatToIntBits(Float.NaN))
            .thenReturn(floatToIntBits(Float.POSITIVE_INFINITY))
            .thenReturn(floatToIntBits(Float.NEGATIVE_INFINITY))
            .thenReturn(2);

        assertEquals(intBitsToFloat(2), source.nextFloat(), 0F);

        verify(random, times(4)).nextInt();
    }

    @Test
    public void nextDouble() {
        when(random.nextLong()).thenReturn(doubleToLongBits(Double.NaN))
            .thenReturn(doubleToLongBits(Double.POSITIVE_INFINITY))
            .thenReturn(doubleToLongBits(Double.NEGATIVE_INFINITY))
            .thenReturn(8L);

        assertEquals(longBitsToDouble(8L), source.nextDouble(), 0D);

        verify(random, times(4)).nextLong();
    }

    @Test
    public void nextBytes() {
        source.nextBytes(1);

        verify(random).nextBytes(new byte[1]);
    }
}

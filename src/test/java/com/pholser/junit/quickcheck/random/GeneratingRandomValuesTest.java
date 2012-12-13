/*
 The MIT License

 Copyright (c) 2010-2012 Paul R. Holser, Jr.

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GeneratingRandomValuesTest {
    private SourceOfRandomness source;
    @Mock private Random random;

    @Before
    public void beforeEach() {
        source = new SourceOfRandomness(random);
    }

    @Test
    public void delegatesToJDKForNextBoolean() {
        source.nextBoolean();

        verify(random).nextBoolean();
    }

    @Test
    public void delegatesToJDKForNextBytes() {
        byte[] bytes = new byte[1];

        source.nextBytes(bytes);

        verify(random).nextBytes(bytes);
    }

    @Test
    public void delegatesToJDKForNextDouble() {
        source.nextDouble();

        verify(random).nextDouble();
    }

    @Test
    public void delegatesToJDKForNextFloat() {
        source.nextFloat();

        verify(random).nextFloat();
    }

    @Test
    public void delegatesToJDKForNextGaussian() {
        source.nextGaussian();

        verify(random).nextGaussian();
    }

    @Test
    public void delegatesToJDKForNextInt() {
        source.nextInt();

        verify(random).nextInt();
    }

    @Test
    public void delegatesToJDKForNextIntFromZeroToN() {
        source.nextInt(2);

        verify(random).nextInt(2);
    }

    @Test
    public void delegatesToJDKForNextLong() {
        source.nextLong();

        verify(random).nextLong();
    }

    @Test
    public void delegatesToJDKForSetSeed() {
        source.setSeed(2L);

        verify(random).setSeed(2L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nextIntWithBackwardsRange() {
        source.nextInt(0, -1);
    }

    @Test
    public void nextIntWithIdenticalMinAndMax() {
        assertEquals(-2, source.nextInt(-2, -2));
    }

    @Test
    public void nextIntInRange() {
        int value = source.nextInt(3, 5);

        verify(random).nextInt(5 - 3 + 1);
        assertThat(value, lessThanOrEqualTo(5));
        assertThat(value, greaterThanOrEqualTo(3));
    }

    @Test
    public void nextBytes() {
        source.nextBytes(1);

        verify(random).nextBytes(new byte[1]);
    }
}

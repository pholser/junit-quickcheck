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

package com.pholser.junit.quickcheck.random;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import static java.util.Arrays.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.runners.MethodSorters.*;

@FixMethodOrder(NAME_ASCENDING)
public class GeneratingRandomValuesTest {
    private SourceOfRandomness source;

    @Before public void beforeEach() {
        source = new SourceOfRandomness(new Random());
        source.setSeed(1L);
    }

    @Test public void delegatesToJDKForNextBoolean() {
        assertTrue(source.nextBoolean());
    }

    @Test public void delegatesToJDKForNextBytes() {
        byte[] bytes = new byte[1];

        source.nextBytes(bytes);

        assertEquals(115, bytes[0]);
    }

    @Test public void delegatesToJDKForNextDouble() {
        assertEquals(0.7308781907032909, source.nextDouble(), 0D);
    }

    @Test public void delegatesToJDKForNextFloat() {
        assertEquals(0.7308782F, source.nextFloat(), 0F);
    }

    @Test public void delegatesToJDKForNextGaussian() {
        assertEquals(1.561581040188955, source.nextGaussian(), 0D);
    }

    @Test public void delegatesToJDKForNextInt() {
        assertEquals(-1155869325, source.nextInt());
    }

    @Test public void delegatesToJDKForNextIntFromZeroToN() {
        assertEquals(1, source.nextInt(2));
    }

    @Test public void delegatesToJDKForNextLong() {
        assertEquals(-4964420948893066024L, source.nextLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nextByteWithBackwardsRange() {
        source.nextByte((byte) 0, (byte) -1);
    }

    @Test public void nextByteWithIdenticalMinAndMax() {
        assertEquals((byte) -2, source.nextByte((byte) -2, (byte) -2));
    }

    @Test public void nextByteInRange() {
        byte value = source.nextByte((byte) 3, (byte) 5);

        assertThat(value, lessThanOrEqualTo((byte) 5));
        assertThat(value, greaterThanOrEqualTo((byte) 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nextShortWithBackwardsRange() {
        source.nextShort((short) 0, (short) -1);
    }

    @Test public void nextShortWithIdenticalMinAndMax() {
        assertEquals((short) -2, source.nextShort((short) -2, (short) -2));
    }

    @Test public void nextShortInRange() {
        short value = source.nextShort((short) 3, (short) 5);

        assertThat(value, lessThanOrEqualTo((short) 5));
        assertThat(value, greaterThanOrEqualTo((short) 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nextCharWithBackwardsRange() {
        source.nextChar('b', 'a');
    }

    @Test public void nextCharWithIdenticalMinAndMax() {
        assertEquals('c', source.nextChar('c', 'c'));
    }

    @Test public void nextCharInRange() {
        char value = source.nextChar('d', 'f');

        assertThat(value, lessThanOrEqualTo('f'));
        assertThat(value, greaterThanOrEqualTo('d'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nextIntWithBackwardsRange() {
        source.nextInt(0, -1);
    }

    @Test public void nextIntWithIdenticalMinAndMax() {
        assertEquals(-2, source.nextInt(-2, -2));
    }

    @Test public void nextIntInRange() {
        int value = source.nextInt(3, 5);

        assertThat(value, lessThanOrEqualTo(5));
        assertThat(value, greaterThanOrEqualTo(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nextLongWithBackwardsRange() {
        source.nextLong(0L, -1L);
    }

    @Test public void nextLongWithIdenticalMinAndMax() {
        assertEquals(-2L, source.nextLong(-2L, -2L));
    }

    @Test public void nextLongInRange() {
        long value = source.nextLong(3L, 5L);

        assertThat(value, lessThanOrEqualTo(5L));
        assertThat(value, greaterThanOrEqualTo(3L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nextFloatWithBackwardsRange() {
        source.nextFloat(0.2F, 0.1F);
    }

    @Test public void nextFloatWithIdenticalMinAndMax() {
        assertEquals(-2F, source.nextFloat(-2F, -2F), 0F);
    }

    @Test public void nextFloatInRange() {
        float value = source.nextFloat(-4.56F, -1.234234F);

        assertThat(value, lessThanOrEqualTo(-1.234234F));
        assertThat(value, greaterThanOrEqualTo(-4.56F));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nextDoubleWithBackwardsRange() {
        source.nextDouble(0.2, 0.1);
    }

    @Test public void nextDoubleWithIdenticalMinAndMax() {
        assertEquals(-2D, source.nextDouble(-2D, -2D), 0D);
    }

    @Test public void nextDoubleInRange() {
        double value = source.nextDouble(-4.56, -1.234234);

        assertThat(value, lessThanOrEqualTo(-1.234234));
        assertThat(value, greaterThanOrEqualTo(-4.56));
    }

    @Test public void nextBytes() {
        assertArrayEquals(new byte[] { 115 }, source.nextBytes(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nextInstantWithBackwardsRange() {
        Instant now = Instant.now();

        source.nextInstant(now, now.minusNanos(1));
    }

    @Test public void nextInstantWithIdenticalMinAndMax() {
        Instant now = Instant.now();

        assertEquals(now, source.nextInstant(now, now));
    }

    @Test public void nextInstantInRange() {
        Instant now = Instant.now();
        Instant later = now.plusNanos(3);

        Instant value = source.nextInstant(now, later);

        assertThat(
            value,
            allOf(
                greaterThanOrEqualTo(now),
                lessThanOrEqualTo(later)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nextDurationWithBackwardsRange() {
        source.nextDuration(
            Duration.of(1, ChronoUnit.NANOS),
            Duration.of(0, ChronoUnit.NANOS));
    }

    @Test public void nextDurationWithIdenticalMinAndMax() {
        Duration d = Duration.of(1, ChronoUnit.NANOS);

        assertEquals(d, source.nextDuration(d, d));
    }

    @Test public void nextDurationInRange() {
        Duration min = Duration.of(1, ChronoUnit.NANOS);
        Duration max = Duration.of(5, ChronoUnit.NANOS);

        Duration value = source.nextDuration(min, max);

        assertThat(
            value,
            allOf(
                greaterThanOrEqualTo(min),
                lessThanOrEqualTo(max)));
    }

    @Test public void samplingArray() {
        assertEquals("c", source.choose(new String[] { "a", "b", "c", "d" }));
    }

    @Test public void samplingCollection() {
        assertEquals(Integer.valueOf(-1), source.choose(asList(-1, -2, -3, -4, -5)));
    }
}

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

package com.pholser.junit.quickcheck.generator.java.lang;

import java.math.BigInteger;
import java.util.function.Function;
import java.util.function.Predicate;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.IntegralGenerator;
import com.pholser.junit.quickcheck.generator.internal.Comparables;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.util.Arrays.*;

import static com.pholser.junit.quickcheck.internal.Reflection.*;

/**
 * Produces values of type {@code long} or {@link Long}.
 */
public class LongGenerator extends IntegralGenerator<Long> {
    private long min = (Long) defaultValueOf(InRange.class, "minLong");
    private long max = (Long) defaultValueOf(InRange.class, "maxLong");

    @SuppressWarnings("unchecked") public LongGenerator() {
        super(asList(Long.class, long.class));
    }

    /**
     * Tells this generator to produce values within a specified minimum and/or
     * maximum, inclusive, with uniform distribution.
     *
     * {@link InRange#min} and {@link InRange#max} take precedence over
     * {@link InRange#minLong()} and {@link InRange#maxLong()}, if non-empty.
     *
     * @param range annotation that gives the range's constraints
     */
    public void configure(InRange range) {
        min = range.min().isEmpty() ? range.minLong() : Long.parseLong(range.min());
        max = range.max().isEmpty() ? range.maxLong() : Long.parseLong(range.max());
    }

    @Override public Long generate(SourceOfRandomness random, GenerationStatus status) {
        return random.nextLong(min, max);
    }

    @Override protected Function<BigInteger, Long> narrow() {
        return BigInteger::longValue;
    }

    @Override protected Predicate<Long> inRange() {
        return Comparables.inRange(min, max);
    }

    @Override protected Long leastMagnitude() {
        return Comparables.leastMagnitude(min, max, 0L);
    }

    @Override protected boolean negative(Long target) {
        return target < 0;
    }

    @Override protected Long negate(Long target) {
        return -target;
    }
}

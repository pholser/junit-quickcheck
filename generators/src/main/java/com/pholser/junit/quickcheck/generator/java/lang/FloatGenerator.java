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

import java.math.BigDecimal;
import java.util.function.Function;
import java.util.function.Predicate;

import com.pholser.junit.quickcheck.generator.DecimalGenerator;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.internal.Comparables;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.util.Arrays.*;

import static com.pholser.junit.quickcheck.internal.Reflection.*;

/**
 * Produces values of type {@code float} or {@link Float}.
 */
public class FloatGenerator extends DecimalGenerator<Float> {
    private float min = (Float) defaultValueOf(InRange.class, "minFloat");
    private float max = (Float) defaultValueOf(InRange.class, "maxFloat");

    @SuppressWarnings("unchecked") public FloatGenerator() {
        super(asList(Float.class, float.class));
    }

    /**
     * Tells this generator to produce values within a specified minimum
     * (inclusive) and/or maximum (exclusive) with uniform distribution.
     *
     * {@link InRange#min} and {@link InRange#max} take precedence over
     * {@link InRange#minFloat()} and {@link InRange#maxFloat()}, if non-empty.
     *
     * @param range annotation that gives the range's constraints
     */
    public void configure(InRange range) {
        min = range.min().isEmpty() ? range.minFloat() : Float.parseFloat(range.min());
        max = range.max().isEmpty() ? range.maxFloat() : Float.parseFloat(range.max());
    }

    @Override public Float generate(SourceOfRandomness random, GenerationStatus status) {
        return random.nextFloat(min, max);
    }

    @Override protected Function<Float, BigDecimal> widen() {
        return BigDecimal::valueOf;
    }

    @Override protected Function<BigDecimal, Float> narrow() {
        return BigDecimal::floatValue;
    }

    @Override protected Predicate<Float> inRange() {
        return Comparables.inRange(min, max);
    }

    @Override protected Float leastMagnitude() {
        return Comparables.leastMagnitude(min, max, 0F);
    }

    @Override protected boolean negative(Float target) {
        return target < 0;
    }

    @Override protected Float negate(Float target) {
        return -target;
    }
}

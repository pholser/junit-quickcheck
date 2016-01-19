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
 * Produces values for theory parameters of type {@code double} or
 * {@link Double}.
 */
public class DoubleGenerator extends DecimalGenerator<Double> {
    private double min = (Double) defaultValueOf(InRange.class, "minDouble");
    private double max = (Double) defaultValueOf(InRange.class, "maxDouble");

    @SuppressWarnings("unchecked") public DoubleGenerator() {
        super(asList(Double.class, double.class));
    }

    /**
     * Tells this generator to produce values within a specified minimum
     * (inclusive) and/or maximum (exclusive) with uniform distribution.
     *
     * {@link InRange#min} and {@link InRange#max} take precedence over
     * {@link InRange#minDouble()} and {@link InRange#maxDouble()},
     * if non-empty.
     *
     * @param range annotation that gives the range's constraints
     */
    public void configure(InRange range) {
        min = range.min().isEmpty() ? range.minDouble() : Double.parseDouble(range.min());
        max = range.max().isEmpty() ? range.maxDouble() : Double.parseDouble(range.max());
    }

    @Override public Double generate(SourceOfRandomness random, GenerationStatus status) {
        return random.nextDouble(min, max);
    }

    @Override protected Function<Double, BigDecimal> widen() {
        return BigDecimal::valueOf;
    }

    @Override protected Function<BigDecimal, Double> narrow() {
        return BigDecimal::doubleValue;
    }

    @Override protected Predicate<Double> inRange() {
        return Comparables.inRange(min, max);
    }

    @Override protected Double leastMagnitude() {
        return Comparables.leastMagnitude(min, max, 0D);
    }

    @Override protected boolean negative(Double target) {
        return target < 0;
    }

    @Override protected Double negate(Double target) {
        return -target;
    }
}

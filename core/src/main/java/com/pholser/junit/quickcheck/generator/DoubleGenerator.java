/*
 The MIT License

 Copyright (c) 2010-2013 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.generator;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.lang.Double.*;
import static java.util.Arrays.*;

/**
 * Produces values for theory parameters of type {@code double} or {@link Double}.
 */
public class DoubleGenerator extends Generator<Double> {
    private double min = -MAX_VALUE;
    private double max = MAX_VALUE;

    @SuppressWarnings("unchecked") public DoubleGenerator() {
        super(asList(double.class, Double.class));
    }

    /**
     * <p>Tells this generator to produce values within a specified {@linkplain InRange#minDouble()}  minimum}
     * (inclusive) and/or {@linkplain InRange#maxDouble()}  maximum} (exclusive), with uniform distribution.</p>
     *
     * <p>If an endpoint of the range is not specified, the generator will use either -{@link Double#MAX_VALUE} or
     * {@link Double#MAX_VALUE} as appropriate.</p>
     *
     * @param range annotation that gives the range's constraints
     */
    public void configure(InRange range) {
        min = range.minDouble();
        max = range.maxDouble();
    }

    @Override public Double generate(SourceOfRandomness random, GenerationStatus status) {
        return random.nextDouble(min, max);
    }
}

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

package com.pholser.junit.quickcheck.generator.java.util;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.java.lang.DoubleGenerator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/**
 * Produces values of type {@link OptionalDouble}.
 */
public class OptionalDoubleGenerator extends Generator<OptionalDouble> {
    private final DoubleGenerator doubleGenerator = new DoubleGenerator();

    public OptionalDoubleGenerator() {
        super(OptionalDouble.class);
    }

    /**
     * Tells this generator to produce values, when
     * {@link OptionalDouble#isPresent() present}, within a specified minimum
     * (inclusive) and/or maximum (exclusive) with uniform distribution.
     *
     * {@link InRange#min} and {@link InRange#max} take precedence over
     * {@link InRange#minDouble()} and {@link InRange#maxDouble()},
     * if non-empty.
     *
     * @param range annotation that gives the range's constraints
     */
    public void configure(InRange range) {
        doubleGenerator.configure(range);
    }

    @Override public OptionalDouble generate(SourceOfRandomness random, GenerationStatus status) {
        double trial = random.nextDouble();
        return trial < 0.25
            ? OptionalDouble.empty()
            : OptionalDouble.of(doubleGenerator.generate(random, status));
    }

    @Override public List<OptionalDouble> doShrink(
        SourceOfRandomness random,
        OptionalDouble larger) {

        if (!larger.isPresent())
            return new ArrayList<>();

        List<OptionalDouble> shrinks = new ArrayList<>();
        shrinks.add(OptionalDouble.empty());
        shrinks.addAll(
            doubleGenerator.shrink(random, larger.getAsDouble())
                .stream()
                .map(OptionalDouble::of)
                .collect(Collectors.toList()));
        return shrinks;
    }
}

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

import static java.lang.Long.*;
import static java.util.Arrays.*;

/**
 * Produces values for theory parameters of type {@code long} or {@link Long}.
 */
public class LongGenerator extends Generator<Long> {
    private long min = MIN_VALUE;
    private long max = MAX_VALUE;

    @SuppressWarnings("unchecked")
    public LongGenerator() {
        super(asList(long.class, Long.class));
    }

    /**
     * <p>Tells this generator to produce values within a specified {@linkplain InRange#minLong() minimum} and/or
     * {@linkplain InRange#maxLong()} maximum}, inclusive, with uniform distribution.</p>
     *
     * <p>If an endpoint of the range is not specified, the generator will use either {@link Long#MIN_VALUE} or
     * {@link Long#MAX_VALUE} as appropriate.</p>
     *
     * @param range annotation that gives the range's constraints
     */
    public void configure(InRange range) {
        min = range.minLong();
        max = range.maxLong();
    }

    @Override
    public Long generate(SourceOfRandomness random, GenerationStatus status) {
        return random.nextLong(min, max);
    }
}

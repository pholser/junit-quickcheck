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

package com.pholser.junit.quickcheck.generator;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.lang.Float.*;
import static java.util.Arrays.*;

/**
 * Produces values for theory parameters of type {@code float} or {@link Float}.
 */
public class FloatGenerator extends Generator<Float> {
    private float min = -MAX_VALUE;
    private float max = MAX_VALUE;

    @SuppressWarnings("unchecked")
    public FloatGenerator() {
        super(asList(float.class, Float.class));
    }

    /**
     * <p>Tells this generator to produce values within a specified {@linkplain InRange#minFloat()}  minimum}
     * (inclusive) and/or {@linkplain InRange#maxFloat()}  maximum} (exclusive), with uniform distribution.</p>
     *
     * <p>If an endpoint of the range is not specified, the generator will use either -{@link Float#MAX_VALUE} or
     * {@link Float#MAX_VALUE} as appropriate.</p>
     *
     * @param range annotation that gives the range's constraints
     */
    public void configure(InRange range) {
        min = range.minFloat();
        max = range.maxFloat();
    }

    @Override
    public Float generate(SourceOfRandomness random, GenerationStatus status) {
        return random.nextFloat(min, max);
    }
}

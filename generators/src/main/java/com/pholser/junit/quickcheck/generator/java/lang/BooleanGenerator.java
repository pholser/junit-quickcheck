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

import java.util.List;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.ValuesOf;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.util.Arrays.*;
import static java.util.Collections.*;

/**
 * Produces values of type {@code boolean} or {@link Boolean}.
 */
public class BooleanGenerator extends Generator<Boolean> {
    private ValuesOf turnOffRandomness;

    @SuppressWarnings("unchecked") public BooleanGenerator() {
        super(asList(Boolean.class, boolean.class));
    }

    /**
     * <p>Tells this generator to generate the values {@code true} and
     * {@code false} on alternating requests.</p>
     *
     * <p>Without this configuration, {@code true} and {@code false} are
     * generated with approximately equal probability.</p>
     *
     * @param flag annotation to turn off random generation and replace it
     * with alternating values
     */
    public void configure(ValuesOf flag) {
        turnOffRandomness = flag;
    }

    @Override public Boolean generate(SourceOfRandomness random, GenerationStatus status) {
        return turnOffRandomness == null ? random.nextBoolean() : status.attempts() % 2 != 0;
    }

    @Override public List<Boolean> doShrink(SourceOfRandomness random, Boolean larger) {
        return larger ? singletonList(false) : emptyList();
    }
}

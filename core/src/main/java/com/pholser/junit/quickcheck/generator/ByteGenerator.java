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

import static java.lang.Byte.*;
import static java.util.Arrays.*;

/**
 * Produces values for theory parameters of type {@code byte} or {@link Byte}.
 */
public class ByteGenerator extends Generator<Byte> {
    private byte min = MIN_VALUE;
    private byte max = MAX_VALUE;

    @SuppressWarnings("unchecked") public ByteGenerator() {
        super(asList(byte.class, Byte.class));
    }

    /**
     * <p>Tells this generator to produce values within a specified {@linkplain InRange#minByte() minimum} and/or
     * {@linkplain InRange#maxByte()} maximum}, inclusive, with uniform distribution.</p>
     *
     * <p>If an endpoint of the range is not specified, the generator will use either {@link Byte#MIN_VALUE} or
     * {@link Byte#MAX_VALUE} as appropriate.</p>
     *
     * @param range annotation that gives the range's constraints
     */
    public void configure(InRange range) {
        min = range.minByte();
        max = range.maxByte();
    }

    @Override public Byte generate(SourceOfRandomness random, GenerationStatus status) {
        return random.nextByte(min, max);
    }
}

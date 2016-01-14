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
import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.util.Collections.*;

/**
 * Produces values of type {@link BitSet}.
 */
public class BitSetGenerator extends Generator<BitSet> {
    public BitSetGenerator() {
        super(BitSet.class);
    }

    @Override public BitSet generate(SourceOfRandomness random, GenerationStatus status) {
        int size = status.size();

        BitSet bits = new BitSet(size);
        for (int i = 0; i < size; ++i)
            bits.set(i, random.nextBoolean());

        return bits;
    }

    @Override public List<BitSet> doShrink(SourceOfRandomness random, BitSet larger) {
        if (larger.length() == 0)
            return emptyList();

        List<BitSet> shrinks = new ArrayList<>();
        shrinks.addAll(larger.stream()
            .mapToObj(i -> larger.get(0, i))
            .collect(Collectors.toList()));
        shrinks.addAll(larger.stream()
            .mapToObj(i -> {
                BitSet smaller = (BitSet) larger.clone();
                smaller.clear(i);
                return smaller;
            })
            .collect(Collectors.toList()));

        return shrinks;
    }
}

/*
 The MIT License

 Copyright (c) 2010-2020 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.test.generator;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.Comparables;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

public class AnInt extends Generator<Integer> {
    private Between range;

    public AnInt() {
        super(asList(Integer.class, int.class));
    }

    public void configure(Between range) {
        this.range = range;
    }

    @Override public Integer generate(
        SourceOfRandomness random,
        GenerationStatus status) {

        return range == null
            ? random.nextInt()
            : random.nextInt(range.min(), range.max());
    }

    @Override public List<Integer> doShrink(
        SourceOfRandomness random,
        Integer larger) {

        return larger == 0
            ? emptyList()
            : Stream.of(larger / 2)
                .filter(this::inRange)
                .collect(toList());
    }

    @Override public BigDecimal magnitude(Object value) {
        return BigDecimal.valueOf(narrow(value));
    }

    private boolean inRange(Integer value) {
        return range == null
            || Comparables.inRange(range.min(), range.max()).test(value);
    }
}

/*
 The MIT License

 Copyright (c) 2010-2018 Paul R. Holser, Jr.

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

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.Comparables;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public class AFooBadShrinks extends Generator<Foo> {
    private Between range;

    public AFooBadShrinks() {
        super(Foo.class);
    }

    @Override public Foo generate(SourceOfRandomness random, GenerationStatus status) {
        return new Foo(
            range == null
                ? random.nextInt()
                : random.nextInt(range.min(), range.max()));
    }

    @Override public List<Foo> doShrink(SourceOfRandomness random, Foo larger) {
        return unshrinkable(larger)
            ? emptyList()
            : Stream.of(new Foo(larger.i(), larger.marked()))
                .filter(f -> !unshrinkable(f))
                .collect(toList());
    }

    @Override public BigDecimal magnitude(Object value) {
        return BigDecimal.valueOf(narrow(value).i());
    }

    public void configure(Between range) {
        this.range = range;
    }

    private boolean unshrinkable(Foo value) {
        return range != null ? !inRange(value) : value.i() == 0;
    }

    private boolean inRange(Foo value) {
        return Comparables.inRange(range.min(), range.max()).test(value.i());
    }
}

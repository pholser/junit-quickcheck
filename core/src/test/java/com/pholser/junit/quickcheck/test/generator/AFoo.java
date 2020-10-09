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

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.GeneratorConfiguration;
import com.pholser.junit.quickcheck.internal.Comparables;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

public class AFoo extends Generator<Foo> {
    private Same value;
    private X x;
    private Between range;

    @Target({ PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE })
    @Retention(RUNTIME)
    @GeneratorConfiguration
    public @interface Same {
        int value();
    }

    public AFoo() {
        super(Foo.class);
    }

    @Override public Foo generate(SourceOfRandomness random, GenerationStatus status) {
        return new Foo(
            value == null
                ? (range == null
                    ? random.nextInt()
                    : random.nextInt(range.min(), range.max()))
                : value.value(),
            x != null);
    }

    @Override public List<Foo> doShrink(SourceOfRandomness random, Foo larger) {
        return unshrinkable(larger)
            ? emptyList()
            : Stream.of(new Foo(larger.i() / 2, larger.marked()))
                .filter(f -> !unshrinkable(f))
                .collect(toList());
    }

    @Override public BigDecimal magnitude(Object value) {
        return BigDecimal.valueOf(narrow(value).i());
    }

    public void configure(Same value) {
        this.value = value;
    }

    public void configure(X x) {
        this.x = x;
    }

    public void configure(Between range) {
        this.range = range;
    }

    private boolean unshrinkable(Foo larger) {
        return value != null ? !inRange(larger) : larger.i() == 0;
    }

    private boolean inRange(Foo larger) {
        return range == null
            || Comparables.inRange(range.min(), range.max()).test(larger.i());
    }
}

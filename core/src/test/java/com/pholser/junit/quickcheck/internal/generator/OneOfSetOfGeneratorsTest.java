/*
 The MIT License

 Copyright (c) 2010-2017 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.internal.generator;

import java.math.BigDecimal;
import java.util.Random;

import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.test.generator.ABigInt;
import com.pholser.junit.quickcheck.test.generator.AByte;
import com.pholser.junit.quickcheck.test.generator.ADecimal;
import com.pholser.junit.quickcheck.test.generator.ADouble;
import com.pholser.junit.quickcheck.test.generator.AFloat;
import com.pholser.junit.quickcheck.test.generator.ALong;
import com.pholser.junit.quickcheck.test.generator.AShort;
import com.pholser.junit.quickcheck.test.generator.AnInt;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class OneOfSetOfGeneratorsTest {
    private GeneratorRepository repo;
    private SourceOfRandomness random;

    @Before public void setUp() {
        random = new SourceOfRandomness(new Random());
        repo = new GeneratorRepository(random)
            .register(new AFloat())
            .register(new ADouble())
            .register(new ADecimal())
            .register(new AnInt())
            .register(new ALong())
            .register(new ABigInt());
    }

    @Test public void choosingFromSubtypes() {
        Generator<? extends Number> g =
            repo.oneOf(Float.class, Double.class, BigDecimal.class);

        Number n = g.generate(random, null);

        assertThat(
            n,
            anyOf(
                instanceOf(Float.class),
                instanceOf(Double.class),
                instanceOf(BigDecimal.class)));
    }

    @Test public void choosingFromGenerators() {
        Generator<? extends Number> g = repo.oneOf(new AByte(), new AShort());

        Number n = g.generate(random, null);

        assertThat(
            n,
            anyOf(instanceOf(Byte.class), instanceOf(Short.class)));
    }
}

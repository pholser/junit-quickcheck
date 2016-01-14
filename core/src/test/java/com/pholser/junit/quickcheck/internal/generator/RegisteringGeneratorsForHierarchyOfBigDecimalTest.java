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

package com.pholser.junit.quickcheck.internal.generator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.test.generator.ADecimal;
import com.pholser.junit.quickcheck.test.generator.AnInt;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static com.pholser.junit.quickcheck.Types.*;
import static com.pholser.junit.quickcheck.internal.generator.Generators.*;

public class RegisteringGeneratorsForHierarchyOfBigDecimalTest {
    @Rule public final MockitoRule mockito = MockitoJUnit.rule();

    private static BigDecimal bigDecimal;
    private static Comparable<BigDecimal> comparable;
    private static Serializable serializable;
    private static Number number;
    private static Object object;

    private GeneratorRepository repo;
    private ADecimal generator;
    @Mock private SourceOfRandomness random;

    @Before public void beforeEach() {
        repo = new GeneratorRepository(random);

        generator = new ADecimal();
        List<Generator<?>> generators = new ArrayList<>();
        generators.add(generator);
        generators.add(new AnInt());
        generators.add(new ZilchGenerator());

        repo.register(generators);
    }

    @Test public void bigDecimal() throws Exception {
        Generator<?> result = repo.generatorFor(typeOf(getClass(), "bigDecimal"));

        assertGenerators(result, generator.getClass());
    }

    @Test public void comparable() throws Exception {
        Generator<?> result = repo.generatorFor(typeOf(getClass(), "comparable"));

        assertGenerators(result, generator.getClass(), AnInt.class);
    }

    @Test public void serializable() throws Exception {
        Generator<?> result = repo.generatorFor(typeOf(getClass(), "serializable"));

        assertGenerators(result, generator.getClass(), AnInt.class);
    }

    @Test public void number() throws Exception {
        Generator<?> result = repo.generatorFor(typeOf(getClass(), "number"));

        assertGenerators(result, generator.getClass(), AnInt.class);
    }

    @Test public void object() throws Exception {
        Generator<?> result = repo.generatorFor(typeOf(getClass(), "object"));

        assertGenerators(result, generator.getClass(), AnInt.class, ZilchGenerator.class);
    }
}

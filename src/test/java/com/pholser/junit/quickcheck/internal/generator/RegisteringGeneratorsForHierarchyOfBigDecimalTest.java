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

package com.pholser.junit.quickcheck.internal.generator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.pholser.junit.quickcheck.generator.BigDecimalGenerator;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.IntegerGenerator;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.collect.Lists.*;
import static com.pholser.junit.quickcheck.internal.generator.Generators.*;

public class RegisteringGeneratorsForHierarchyOfBigDecimalTest {
    private GeneratorRepository repo;
    private BigDecimalGenerator generator;

    @Before
    public void setUp() {
        repo = new GeneratorRepository();

        generator = new BigDecimalGenerator();
        List<Generator<?>> generators = newArrayList();
        generators.add(generator);
        generators.add(new IntegerGenerator());

        repo.add(generators);
    }

    @Test
    public void bigDecimal() {
        Generator<?> result = repo.generatorFor(BigDecimal.class);

        assertGenerators(result, generator.getClass());
    }

    @Test
    public void comparable() {
        Generator<?> result = repo.generatorFor(Comparable.class);

        assertGenerators(result, generator.getClass(), IntegerGenerator.class);
    }

    @Test
    public void serializable() {
        Generator<?> result = repo.generatorFor(Serializable.class);

        assertGenerators(result, generator.getClass(), IntegerGenerator.class);
    }

    @Test
    public void number() {
        Generator<?> result = repo.generatorFor(Number.class);

        assertGenerators(result, generator.getClass(), IntegerGenerator.class);
    }

    @Test
    public void object() {
        Generator<?> result = repo.generatorFor(Object.class);

        assertGenerators(result, generator.getClass(), IntegerGenerator.class);
    }
}

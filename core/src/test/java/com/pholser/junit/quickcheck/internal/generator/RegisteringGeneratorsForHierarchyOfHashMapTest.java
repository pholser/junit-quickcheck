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

package com.pholser.junit.quickcheck.internal.generator;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.test.generator.TestHashMapGenerator;
import com.pholser.junit.quickcheck.test.generator.TestIntegerGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.google.common.collect.Lists.*;
import static com.pholser.junit.quickcheck.internal.generator.Generators.*;

@RunWith(MockitoJUnitRunner.class)
public class RegisteringGeneratorsForHierarchyOfHashMapTest {
    private GeneratorRepository repo;
    private TestHashMapGenerator generator;
    @Mock private SourceOfRandomness random;

    @Before public void beforeEach() {
        repo = new GeneratorRepository(random);

        generator = new TestHashMapGenerator();
        List<Generator<?>> generators = newArrayList();
        generators.add(generator);
        generators.add(new TestIntegerGenerator());
        generators.add(new ZilchGenerator());

        repo.register(generators);
    }

    @Test public void abstractMap() {
        Generator<?> result = repo.generatorFor(AbstractMap.class);

        assertGenerators(result, generator.getClass());
    }

    @Test public void map() {
        Generator<?> result = repo.generatorFor(Map.class);

        assertGenerators(result, generator.getClass());
    }

    @Test public void cloneable() {
        Generator<?> result = repo.generatorFor(Cloneable.class);

        assertGenerators(result, generator.getClass());
    }

    @Test public void serializable() {
        Generator<?> result = repo.generatorFor(Serializable.class);

        assertGenerators(result, generator.getClass(), TestIntegerGenerator.class);
    }

    @Test public void mapsDoNotGetRetrievedForObjectType() {
        Generator<?> result = repo.generatorFor(Object.class);

        assertGenerators(result, TestIntegerGenerator.class);
    }
}

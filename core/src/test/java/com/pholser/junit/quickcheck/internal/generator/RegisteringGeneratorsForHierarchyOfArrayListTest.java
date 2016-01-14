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
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.test.generator.AList;
import com.pholser.junit.quickcheck.test.generator.AnInt;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static com.pholser.junit.quickcheck.Types.*;
import static com.pholser.junit.quickcheck.internal.generator.Generators.*;

public class RegisteringGeneratorsForHierarchyOfArrayListTest {
    @Rule public final MockitoRule mockito = MockitoJUnit.rule();

    private static AbstractList<?> abstractList;
    private static List<?> list;
    private static RandomAccess randomAccess;
    private static Cloneable cloneable;
    private static Serializable serializable;
    private static AbstractCollection<?> abstractCollection;
    private static Collection<?> collection;
    private static Iterable<?> iterable;
    private static Object object;

    private GeneratorRepository repo;
    private AList generator;
    @Mock private SourceOfRandomness random;

    @Before public void beforeEach() {
        repo = new GeneratorRepository(random);

        generator = new AList();
        List<Generator<?>> generators = new ArrayList<>();
        generators.add(generator);
        generators.add(new AnInt());
        generators.add(new ZilchGenerator());

        repo.register(generators);
    }

    @Test public void abstractList() throws Exception {
        Generator<?> result = repo.generatorFor(typeOf(getClass(), "abstractList"));

        assertGenerators(result, generator.getClass());
    }

    @Test public void list() throws Exception {
        Generator<?> result = repo.generatorFor(typeOf(getClass(), "list"));

        assertGenerators(result, generator.getClass());
    }

    @Test public void randomAccess() throws Exception {
        Generator<?> result = repo.generatorFor(typeOf(getClass(), "randomAccess"));

        assertGenerators(result, generator.getClass());
    }

    @Test public void cloneable() throws Exception {
        Generator<?> result = repo.generatorFor(typeOf(getClass(), "cloneable"));

        assertGenerators(result, generator.getClass());
    }

    @Test public void serializable() throws Exception {
        Generator<?> result = repo.generatorFor(typeOf(getClass(), "serializable"));

        assertGenerators(result, generator.getClass(), AnInt.class);
    }

    @Test public void abstractCollection() throws Exception {
        Generator<?> result = repo.generatorFor(typeOf(getClass(), "abstractCollection"));

        assertGenerators(result, generator.getClass());
    }

    @Test public void collection() throws Exception {
        Generator<?> result = repo.generatorFor(typeOf(getClass(), "collection"));

        assertGenerators(result, generator.getClass());
    }

    @Test public void iterable() throws Exception {
        Generator<?> result = repo.generatorFor(typeOf(getClass(), "iterable"));

        assertGenerators(result, generator.getClass());
    }

    @Test public void objectType() throws Exception {
        Generator<?> result = repo.generatorFor(typeOf(getClass(), "object"));

        assertGenerators(result, AnInt.class, ZilchGenerator.class);
    }
}

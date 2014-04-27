/*
 The MIT License

 Copyright (c) 2010-2014 Paul R. Holser, Jr.

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

import java.util.HashMap;
import java.util.List;

import com.pholser.junit.quickcheck.generator.ComponentizedGenerator;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.test.generator.TestIntegerGenerator;
import com.pholser.junit.quickcheck.test.generator.TestStringGenerator;
import org.javaruntype.type.TypeParameter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.google.common.collect.Lists.*;
import static com.pholser.junit.quickcheck.internal.generator.Generators.*;
import static com.pholser.junit.quickcheck.reflect.ParameterizedTypeImpl.*;
import static com.pholser.junit.quickcheck.reflect.WildcardTypeImpl.*;

@RunWith(MockitoJUnitRunner.class)
public class ConstrainingWhatGeneratorsCanAcceptCertainComponentsTest {
    private GeneratorRepository repo;
    @Mock private SourceOfRandomness random;

    @Before public void beforeEach() {
        repo = new GeneratorRepository(random);

        List<Generator<?>> generators = newArrayList();
        generators.add(new HashMapGenerator<Object, Object>());
        generators.add(new StringKeyHashMapGenerator<Object>());
        generators.add(new FailedStringKeyHashMapGenerator<Object>());
        generators.add(new TestStringGenerator());
        generators.add(new TestIntegerGenerator());
        generators.add(new ZilchGenerator());

        repo.register(generators);
    }

    @Test public void rawHashMapType() {
        Generator<?> result = repo.generatorFor(HashMap.class);

        assertGenerators(result, HashMapGenerator.class, StringKeyHashMapGenerator.class);
    }

    @Test public void stringKeyHashMapType() {
        Generator<?> result = repo.generatorFor(parameterized(HashMap.class).on(String.class, huh()));

        assertGenerators(result, HashMapGenerator.class, StringKeyHashMapGenerator.class);
    }

    @Test public void integerKeyHashMapType() {
        Generator<?> result = repo.generatorFor(parameterized(HashMap.class).on(Integer.class, huh()));

        assertGenerators(result, HashMapGenerator.class);
    }

    @Test public void extendsStringKeyMapType() {
        Generator<?> result = repo.generatorFor(parameterized(HashMap.class).on(extendsOf(String.class), huh()));

        assertGenerators(result, HashMapGenerator.class, StringKeyHashMapGenerator.class);
    }

    @Test public void superStringKeyMapType() {
        Generator<?> result = repo.generatorFor(parameterized(HashMap.class).on(superOf(String.class), huh()));

        assertGenerators(result, HashMapGenerator.class, StringKeyHashMapGenerator.class);
    }

    @Test public void huhKeyMapType() {
        Generator<?> result = repo.generatorFor(parameterized(HashMap.class).on(huh(), huh()));

        assertGenerators(result, HashMapGenerator.class, StringKeyHashMapGenerator.class);
    }

    public static class HashMapGenerator<K, V> extends ComponentizedGenerator<HashMap> {
        public HashMapGenerator() {
            super(HashMap.class);
        }

        @Override public HashMap<K, V> generate(SourceOfRandomness random, GenerationStatus status) {
            return new HashMap<K, V>();
        }

        @Override public int numberOfNeededComponents() {
            return 2;
        }
    }

    public static class StringKeyHashMapGenerator<V> extends HashMapGenerator<String, V> {
        @Override public HashMap<String, V> generate(SourceOfRandomness random, GenerationStatus status) {
            return new HashMap<String, V>();
        }

        @Override public boolean canGenerateForParametersOfTypes(List<TypeParameter<?>> typeParameters) {
            return super.canGenerateForParametersOfTypes(typeParameters)
                && compatibleWithTypeParameter(typeParameters.get(0), String.class);
        }
    }

    public static class FailedStringKeyHashMapGenerator<V> extends HashMapGenerator<String, V> {
        @Override public HashMap<String, V> generate(SourceOfRandomness random, GenerationStatus status) {
            return new HashMap<String, V>();
        }

        @Override public boolean canGenerateForParametersOfTypes(List<TypeParameter<?>> typeParameters) {
            return super.canGenerateForParametersOfTypes(typeParameters)
                    && compatibleWithTypeParameter(typeParameters.get(0), String.class);
        }

        @Override public int numberOfNeededComponents() {
            return 1;
        }
    }
}

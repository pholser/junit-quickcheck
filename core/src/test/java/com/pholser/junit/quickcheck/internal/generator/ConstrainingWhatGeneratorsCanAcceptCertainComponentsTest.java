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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pholser.junit.quickcheck.generator.ComponentizedGenerator;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.test.generator.AString;
import com.pholser.junit.quickcheck.test.generator.AnInt;
import org.javaruntype.type.TypeParameter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static com.pholser.junit.quickcheck.Types.*;
import static com.pholser.junit.quickcheck.internal.generator.Generators.*;

public class ConstrainingWhatGeneratorsCanAcceptCertainComponentsTest {
    @Rule public final MockitoRule mockito = MockitoJUnit.rule();

    private static HashMap raw;
    private static HashMap<String, ?> stringKey;
    private static HashMap<Integer, ?> integerKey;
    private static HashMap<? extends String, ?> extendsStringKey;
    private static HashMap<? super String, ?> superStringKey;
    private static HashMap<?, ?> huhKey;

    private GeneratorRepository repo;
    @Mock private SourceOfRandomness random;

    @Before public void beforeEach() {
        repo = new GeneratorRepository(random);

        List<Generator<?>> generators = new ArrayList<>();
        generators.add(new HashMapGenerator<>());
        generators.add(new StringKeyHashMapGenerator<>());
        generators.add(new FailedStringKeyHashMapGenerator<>());
        generators.add(new AString());
        generators.add(new AnInt());
        generators.add(new ZilchGenerator());

        repo.register(generators);
    }

    @Test public void rawHashMapType() throws Exception {
        Generator<?> result = repo.generatorFor(typeOf(getClass(), "raw"));

        assertGenerators(result, HashMapGenerator.class, StringKeyHashMapGenerator.class);
    }

    @Test public void stringKeyHashMapType() throws Exception {
        Generator<?> result = repo.generatorFor(typeOf(getClass(), "stringKey"));

        assertGenerators(result, HashMapGenerator.class, StringKeyHashMapGenerator.class);
    }

    @Test public void integerKeyHashMapType() throws Exception {
        Generator<?> result = repo.generatorFor(typeOf(getClass(), "integerKey"));

        assertGenerators(result, HashMapGenerator.class);
    }

    @Test public void extendsStringKeyMapType() throws Exception {
        Generator<?> result = repo.generatorFor(typeOf(getClass(), "extendsStringKey"));

        assertGenerators(result, HashMapGenerator.class, StringKeyHashMapGenerator.class);
    }

    @Test public void superStringKeyMapType() throws Exception {
        Generator<?> result = repo.generatorFor(typeOf(getClass(), "superStringKey"));

        assertGenerators(result, HashMapGenerator.class, StringKeyHashMapGenerator.class);
    }

    @Test public void huhKeyMapType() throws Exception {
        Generator<?> result = repo.generatorFor(typeOf(getClass(), "huhKey"));

        assertGenerators(result, HashMapGenerator.class, StringKeyHashMapGenerator.class);
    }

    public static class HashMapGenerator<K, V> extends ComponentizedGenerator<HashMap> {
        public HashMapGenerator() {
            super(HashMap.class);
        }

        @Override public HashMap<K, V> generate(SourceOfRandomness random, GenerationStatus status) {
            return new HashMap<>();
        }

        @Override public int numberOfNeededComponents() {
            return 2;
        }
    }

    public static class StringKeyHashMapGenerator<V> extends HashMapGenerator<String, V> {
        @Override public HashMap<String, V> generate(SourceOfRandomness random, GenerationStatus status) {
            return new HashMap<>();
        }

        @Override public boolean canGenerateForParametersOfTypes(List<TypeParameter<?>> typeParameters) {
            return super.canGenerateForParametersOfTypes(typeParameters)
                && compatibleWithTypeParameter(typeParameters.get(0), String.class);
        }
    }

    public static class FailedStringKeyHashMapGenerator<V> extends HashMapGenerator<String, V> {
        @Override public HashMap<String, V> generate(SourceOfRandomness random, GenerationStatus status) {
            return new HashMap<>();
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

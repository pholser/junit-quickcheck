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

import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.test.generator.ABox;
import com.pholser.junit.quickcheck.test.generator.AFoo;
import com.pholser.junit.quickcheck.test.generator.AnotherBox;
import com.pholser.junit.quickcheck.test.generator.Box;
import com.pholser.junit.quickcheck.test.generator.Foo;
import com.pholser.junit.quickcheck.test.generator.TestGeneratorSource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static com.pholser.junit.quickcheck.Types.*;
import static com.pholser.junit.quickcheck.internal.generator.Generators.*;

public class RegisteringGeneratorsWithServiceLoaderTest {
    @Rule public final MockitoRule mockito = MockitoJUnit.rule();

    private static Foo foo;
    private static Box<?> box;

    private GeneratorRepository repo;
    @Mock private SourceOfRandomness random;

    @Before public void beforeEach() {
        repo = new GeneratorRepository(random);
        repo.register(new TestGeneratorSource()).register(new ServiceLoaderGeneratorSource());
    }

    @Test public void bringsInTypesForWhichASingleGeneratorExists() throws Exception {
        assertGenerators(
            repo.generatorFor(typeOf(getClass(), "foo")),
            AFoo.class);
    }

    @Test public void bringsInTypesForWhichMultipleGeneratorsMayExist() throws Exception {
        assertGenerators(
            repo.generatorFor(typeOf(getClass(), "box")),
            ABox.class,
            AnotherBox.class);
    }
}

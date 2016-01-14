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

package com.pholser.junit.quickcheck.guava.generator;

import java.util.List;
import java.util.Random;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.generator.CorePropertyParameterTest;
import com.pholser.junit.quickcheck.internal.generator.ServiceLoaderGeneratorSource;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static com.google.common.collect.Lists.*;
import static com.pholser.junit.quickcheck.Types.*;
import static org.junit.Assert.*;

public class FunctionOfStringToIntPropertyParameterTest extends CorePropertyParameterTest {
    public static final Function<String, Integer> TYPE_BEARER = null;

    private static Integer integer;

    @Override protected void primeSourceOfRandomness() {
        // nothing to do here
    }

    @Override protected int trials() {
        return 2;
    }

    @Override protected List<?> randomValues() {
        return newArrayList(new Object(), new Object());
    }

    @Override public void verifyInteractionWithRandomness() {
    }

    @Override protected Iterable<Generator<?>> auxiliaryGeneratorSource() {
        return new ServiceLoaderGeneratorSource();
    }

    @Override protected void verifyEquivalenceOfPropertyParameter(int index, Object expected, Object actual)
        throws Exception {
        @SuppressWarnings("unchecked")
        Function<String, Integer> f = (Function<String, Integer>) actual;

        String argument = "foobar";

        SourceOfRandomness source = new SourceOfRandomness(new Random());
        source.setSeed(Objects.hashCode(argument));
        Integer value = (Integer) repository.generatorFor(typeOf(getClass(), "integer")).generate(source, null);

        for (int i = 0; i < 10000; ++i)
            assertEquals(value, f.apply(argument));
    }
}

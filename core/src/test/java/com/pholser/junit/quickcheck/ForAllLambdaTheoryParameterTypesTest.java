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

package com.pholser.junit.quickcheck;

import java.util.Random;

import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.test.generator.Box;
import com.pholser.junit.quickcheck.test.generator.Foo;
import com.pholser.junit.quickcheck.test.generator.FooGenerator;
import com.pholser.junit.quickcheck.test.generator.FooUnboxer;
import org.junit.Test;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Annotations.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ForAllLambdaTheoryParameterTypesTest {
    @Test public void unboxingAFoo() throws Exception {
        assertThat(testResult(UnboxingAFoo.class), isSuccessful());
        assertEquals(defaultSampleSize(), UnboxingAFoo.iterations);
    }

    @RunWith(Theories.class)
    public static class UnboxingAFoo {
        static int iterations;

        @Theory public void shouldHold(@ForAll FooUnboxer b) {
            ++iterations;

            @SuppressWarnings("unchecked")
            Foo value = functionValue(new FooGenerator(), new Object[] { new Box<Foo>(new Foo(2)) });
            for (int i = 0; i < 10000; ++i)
                assertEquals(value, b.unbox(new Box<Foo>(new Foo(2))));
        }
    }

    private static <T> T functionValue(Generator<T> generator, Object[] args) {
        Random r = new Random();
        r.setSeed(java.util.Arrays.hashCode(args));
        return generator.generate(new SourceOfRandomness(r), null);
    }
}

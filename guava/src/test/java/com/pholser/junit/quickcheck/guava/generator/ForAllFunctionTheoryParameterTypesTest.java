/*
 The MIT License

 Copyright (c) 2010-2015 Paul R. Holser, Jr.

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

import com.google.common.base.Function;
import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.internal.Zilch;
import com.pholser.junit.quickcheck.test.generator.Box;
import com.pholser.junit.quickcheck.test.generator.Foo;
import org.junit.Test;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Annotations.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ForAllFunctionTheoryParameterTypesTest {
    @Test public void fooToZilch() throws Exception {
        assertThat(testResult(FooToZilch.class), isSuccessful());
        assertEquals(defaultSampleSize(), FooToZilch.iterations);
    }

    @RunWith(Theories.class)
    public static class FooToZilch {
        static int iterations;

        @Theory public void shouldHold(@ForAll Function<Foo, Zilch> f) {
            ++iterations;

            Foo foo = new Foo(2);
            Zilch result = f.apply(foo);

            for (int i = 0; i < 10000; ++i)
                assertEquals(result, f.apply(foo));
        }
    }

    @Test public void superShortToInteger() throws Exception {
        assertThat(testResult(SuperShortToExtendsInteger.class), isSuccessful());
        assertEquals(defaultSampleSize(), SuperShortToExtendsInteger.iterations);
    }

    @RunWith(Theories.class)
    public static class SuperShortToExtendsInteger {
        static int iterations;

        @Theory public void shouldHold(@ForAll Function<? super Foo, ? extends Zilch> f) {
            ++iterations;

            Foo foo = new Foo(2);
            Zilch result = f.apply(foo);

            for (int i = 0; i < 10000; ++i)
                assertEquals(result, f.apply(foo));
        }
    }

    @Test public void arrayOfFunction() throws Exception {
        assertThat(testResult(ArrayOfFunction.class), isSuccessful());
        assertEquals(defaultSampleSize(), SuperShortToExtendsInteger.iterations);
    }

    @RunWith(Theories.class)
    public static class ArrayOfFunction {
        static int iterations;

        @SuppressWarnings("unchecked")
        @Theory public void shouldHold(@ForAll Function<?, ?>[] functions) {
            ++iterations;

            Box<Zilch> box = new Box<>(Zilch.INSTANCE);

            for (Function each : functions)
                each.apply(box);
        }
    }
}

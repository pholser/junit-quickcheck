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

package com.pholser.junit.quickcheck.guava.generator;

import com.google.common.base.Function;
import com.pholser.junit.quickcheck.ForAll;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Annotations.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ForAllFunctionTheoryParameterTypesTest {
    @Test public void stringToInteger() throws Exception {
        assertThat(testResult(StringToInteger.class), isSuccessful());
        assertEquals(defaultSampleSize(), StringToInteger.iterations);
    }

    @RunWith(Theories.class)
    public static class StringToInteger {
        static int iterations;

        @Theory public void shouldHold(@ForAll Function<String, Integer> f) {
            ++iterations;

            Integer result = f.apply("foo");
            for (int i = 0; i < 10000; ++i)
                assertEquals(result, f.apply("foo"));
        }
    }

    @Test public void superShortToInteger() throws Exception {
        assertThat(testResult(SuperShortToExtendsInteger.class), isSuccessful());
        assertEquals(defaultSampleSize(), SuperShortToExtendsInteger.iterations);
    }

    @RunWith(Theories.class)
    public static class SuperShortToExtendsInteger {
        static int iterations;

        @Theory public void shouldHold(@ForAll Function<? super Short, ? extends Integer> f) {
            ++iterations;

            Integer result = f.apply((short) 4);
            for (int i = 0; i < 10000; ++i)
                assertEquals(result, f.apply((short) 4));
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

            for (Function each : functions) {
                each.apply("foo");
            }
        }
    }
}

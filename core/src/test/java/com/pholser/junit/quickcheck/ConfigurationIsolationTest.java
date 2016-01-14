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

package com.pholser.junit.quickcheck;

import com.pholser.junit.quickcheck.test.generator.Box;
import com.pholser.junit.quickcheck.test.generator.Foo;
import org.junit.Test;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Annotations.*;
import static com.pholser.junit.quickcheck.test.generator.AFoo.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

@Deprecated
public class ConfigurationIsolationTest {
    @Test public void acrossParametersOfSameType() throws Exception {
        assertThat(testResult(ParametersOfSameType.class), isSuccessful());
        assertEquals(defaultSampleSize() * defaultSampleSize(), ParametersOfSameType.iterations);
    }

    @RunWith(Theories.class)
    public static class ParametersOfSameType {
        static int iterations;

        @Theory public void holds(@ForAll @Same(2) Foo first, @ForAll @Same(3) Foo second) {
            ++iterations;

            assertEquals(2, first.i());
            assertEquals(3, second.i());
        }
    }

    @Test public void acrossParametersOfSameTypeWithOneConstant() throws Exception {
        assertThat(testResult(ParametersOfSameTypeWithOneConstant.class), isSuccessful());
        assertEquals(defaultSampleSize() * defaultSampleSize(), ParametersOfSameTypeWithOneConstant.iterations);
    }

    @RunWith(Theories.class)
    public static class ParametersOfSameTypeWithOneConstant {
        static int iterations;

        @Theory public void holds(@ForAll @Same(2) Foo first, @ForAll Foo second) {
            ++iterations;

            assertEquals(2, first.i());
            assertNotEquals(2, second.i());
        }
    }

    @Test public void acrossParametersOfSameParameterizedType() throws Exception {
        assertThat(testResult(ParametersOfSameParameterizedType.class), isSuccessful());
        assertEquals(defaultSampleSize() * defaultSampleSize(), ParametersOfSameParameterizedType.iterations);
    }

    @RunWith(Theories.class)
    public static class ParametersOfSameParameterizedType {
        static int iterations;

        @Theory public void holds(@ForAll Box<@Same(2) Foo> first, @ForAll Box<@Same(3) Foo> second) {
            ++iterations;

            assertEquals(2, first.contents().i());
            assertEquals(3, second.contents().i());
        }
    }

    @Test public void acrossParametersOfSameParameterizedTypeWithOneConstant() throws Exception {
        assertThat(testResult(ParametersOfSameParameterizedTypeWithOneConstant.class), isSuccessful());
        assertEquals(
            defaultSampleSize() * defaultSampleSize(),
            ParametersOfSameParameterizedTypeWithOneConstant.iterations);
    }

    @RunWith(Theories.class)
    public static class ParametersOfSameParameterizedTypeWithOneConstant {
        static int iterations;

        @Theory public void holds(@ForAll Box<@Same(2) Foo> first, @ForAll Box<Foo> second) {
            ++iterations;

            assertEquals(2, first.contents().i());
            assertNotEquals(2, second.contents().i());
        }
    }

    @Test public void acrossParametersOfParametersOfParameterizedTypeAndUnparameterizedTypeWithOneConstant()
        throws Exception {

        assertThat(
            testResult(ParametersOfParameterizedTypeAndUnparameterizedTypeWithOneConstant.class),
            isSuccessful());
        assertEquals(
            defaultSampleSize() * defaultSampleSize(),
            ParametersOfParameterizedTypeAndUnparameterizedTypeWithOneConstant.iterations);
    }

    @RunWith(Theories.class)
    public static class ParametersOfParameterizedTypeAndUnparameterizedTypeWithOneConstant {
        static int iterations;

        @Theory public void holds(@ForAll Box<@Same(2) Foo> first, @ForAll Foo second) {
            ++iterations;

            assertEquals(2, first.contents().i());
            assertNotEquals(2, second.i());
        }
    }

    @Test public void acrossParametersOfSameArrayType() throws Exception {
        assertThat(testResult(ParametersOfSameArrayType.class), isSuccessful());
        assertEquals(defaultSampleSize() * defaultSampleSize(), ParametersOfSameArrayType.iterations);
    }

    @RunWith(Theories.class)
    public static class ParametersOfSameArrayType {
        static int iterations;

        @Theory public void holds(@ForAll @Same(2) Foo[] first, @ForAll @Same(3) Foo[][] second) {
            ++iterations;

            for (Foo f : first)
                assertEquals(2, f.i());
            for (Foo[] f : second) {
                for (Foo g : f)
                    assertEquals(3, g.i());
            }
        }
    }

    @Test public void acrossParametersOfSameArrayTypeWithOneConstant() throws Exception {
        assertThat(testResult(ParametersOfSameArrayTypeWithOneConstant.class), isSuccessful());
        assertEquals(
            defaultSampleSize() * defaultSampleSize(),
            ParametersOfSameArrayTypeWithOneConstant.iterations);
    }

    @RunWith(Theories.class)
    public static class ParametersOfSameArrayTypeWithOneConstant {
        static int iterations;

        @Theory public void holds(@ForAll @Same(2) Foo[] first, @ForAll Foo[] second) {
            ++iterations;

            for (Foo f : first)
                assertEquals(2, f.i());
            for (Foo f : second)
                assertNotEquals(2, f.i());
        }
    }

    @Test public void acrossParametersOfParametersOfArrayTypeAndTypeWithOneConstant() throws Exception {
        assertThat(testResult(ParametersOfParametersOfArrayTypeAndTypeWithOneConstant.class), isSuccessful());
        assertEquals(
            defaultSampleSize() * defaultSampleSize(),
            ParametersOfParametersOfArrayTypeAndTypeWithOneConstant.iterations);
    }

    @RunWith(Theories.class)
    public static class ParametersOfParametersOfArrayTypeAndTypeWithOneConstant {
        static int iterations;

        @Theory public void holds(@ForAll @Same(2) Foo[] first, @ForAll Foo second) {
            ++iterations;

            for (Foo f : first)
                assertEquals(2, f.i());
            assertNotEquals(2, second.i());
        }
    }
}

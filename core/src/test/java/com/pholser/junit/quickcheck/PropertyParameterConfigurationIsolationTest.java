/*
 The MIT License

 Copyright (c) 2010-2021 Paul R. Holser, Jr.

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

import static com.pholser.junit.quickcheck.Annotations.defaultPropertyTrialCount;
import static com.pholser.junit.quickcheck.test.generator.AFoo.Same;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.Box;
import com.pholser.junit.quickcheck.test.generator.Foo;
import org.junit.Test;
import org.junit.runner.RunWith;

public class PropertyParameterConfigurationIsolationTest {
    @Test public void acrossParametersOfSameType() throws Exception {
        assertThat(testResult(ParametersOfSameType.class), isSuccessful());
        assertEquals(
            defaultPropertyTrialCount(),
            ParametersOfSameType.iterations);
        ParametersOfSameType.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ParametersOfSameType {
        static int iterations;

        @Property public void holds(@Same(2) Foo first, @Same(3) Foo second) {
            ++iterations;

            assertEquals(2, first.i());
            assertEquals(3, second.i());
        }
    }

    @Test public void acrossParametersOfSameTypeWithOneConstant()
        throws Exception {

        assertThat(
            testResult(ParametersOfSameTypeWithOneConstant.class),
            isSuccessful());
        assertEquals(
            defaultPropertyTrialCount(),
            ParametersOfSameTypeWithOneConstant.iterations);
        ParametersOfSameTypeWithOneConstant.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ParametersOfSameTypeWithOneConstant {
        static int iterations;

        @Property public void holds(@Same(2) Foo first, Foo second) {
            ++iterations;

            assertEquals(2, first.i());
            assertNotEquals(2, second.i());
        }
    }

    @Test public void acrossParametersOfSameParameterizedType()
        throws Exception {

        assertThat(
            testResult(ParametersOfSameParameterizedType.class),
            isSuccessful());
        assertEquals(
            defaultPropertyTrialCount(),
            ParametersOfSameParameterizedType.iterations);
        ParametersOfSameParameterizedType.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ParametersOfSameParameterizedType {
        static int iterations;

        @Property public void holds(
            Box<@Same(2) Foo> first, Box<@Same(3) Foo> second) {

            ++iterations;

            assertEquals(2, first.contents().i());
            assertEquals(3, second.contents().i());
        }
    }

    @Test public void acrossParametersOfSameParameterizedTypeWithOneConstant()
        throws Exception {

        assertThat(
            testResult(ParametersOfSameParameterizedTypeWithOneConstant.class),
            isSuccessful());
        assertEquals(
            defaultPropertyTrialCount(),
            ParametersOfSameParameterizedTypeWithOneConstant.iterations);
        ParametersOfSameParameterizedTypeWithOneConstant.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ParametersOfSameParameterizedTypeWithOneConstant {
        static int iterations;

        @Property public void holds(Box<@Same(2) Foo> first, Box<Foo> second) {
            ++iterations;

            assertEquals(2, first.contents().i());
            assertNotEquals(2, second.contents().i());
        }
    }

    @Test public void
    acrossParametersOfParameterizedAndNotTypesWithOneConstant()
    throws Exception {
        assertThat(
            testResult(
                ParametersOfParameterizedAndNotTypesWithOneConstant.class),
            isSuccessful());
        assertEquals(
            defaultPropertyTrialCount(),
            ParametersOfParameterizedAndNotTypesWithOneConstant.iterations);
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ParametersOfParameterizedAndNotTypesWithOneConstant {
        static int iterations;

        @Property public void holds(Box<@Same(2) Foo> first, Foo second) {
            ++iterations;

            assertEquals(2, first.contents().i());
            assertNotEquals(2, second.i());
        }
    }

    @Test public void acrossParametersOfSameArrayType() throws Exception {
        assertThat(
            testResult(ParametersOfSameArrayType.class),
            isSuccessful());
        assertEquals(
            defaultPropertyTrialCount(),
            ParametersOfSameArrayType.iterations);
        ParametersOfSameArrayType.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ParametersOfSameArrayType {
        static int iterations;

        @Property public void holds(
            @Same(2) Foo[] first,
            @Same(3) Foo[][] second) {

            ++iterations;

            for (Foo f : first)
                assertEquals(2, f.i());
            for (Foo[] f : second) {
                for (Foo g : f)
                    assertEquals(3, g.i());
            }
        }
    }

    @Test public void acrossParametersOfSameArrayTypeWithOneConstant()
        throws Exception {

        assertThat(
            testResult(ParametersOfSameArrayTypeWithOneConstant.class),
            isSuccessful());
        assertEquals(
            defaultPropertyTrialCount(),
            ParametersOfSameArrayTypeWithOneConstant.iterations);
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ParametersOfSameArrayTypeWithOneConstant {
        static int iterations;

        @Property public void holds(@Same(2) Foo[] first, Foo[] second) {
            ++iterations;

            for (Foo f : first)
                assertEquals(2, f.i());
            for (Foo f : second)
                assertNotEquals(2, f.i());
        }
    }

    @Test public void acrossParametersOfArrayTypeAndTypeWithOneConstant()
        throws Exception {

        assertThat(
            testResult(ParametersOfArrayTypeAndTypeWithOneConstant.class),
            isSuccessful());
        assertEquals(
            defaultPropertyTrialCount(),
            ParametersOfArrayTypeAndTypeWithOneConstant.iterations);
        ParametersOfArrayTypeAndTypeWithOneConstant.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ParametersOfArrayTypeAndTypeWithOneConstant {
        static int iterations;

        @Property public void holds(@Same(2) Foo[] first, Foo second) {
            ++iterations;

            for (Foo f : first)
                assertEquals(2, f.i());
            assertNotEquals(2, second.i());
        }
    }
}

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

import java.util.ArrayList;
import java.util.List;

import com.pholser.junit.quickcheck.generator.ValuesOf;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Annotations.*;
import static com.pholser.junit.quickcheck.EnumPropertyParameterTypesTest.TestEnum.*;
import static com.pholser.junit.quickcheck.Lists.*;
import static java.util.Arrays.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class EnumPropertyParameterTypesTest {
    public enum TestEnum {
        E1, E2, E3, E4, E5
    }

    @Test public void usesRegularTrialCount() throws Exception {
        assertThat(testResult(EnumTester.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), EnumTester.iterations);
    }

    @RunWith(JUnitQuickcheck.class)
    public static class EnumTester {
        static int iterations;

        @Property public void shouldHold(TestEnum e) {
            ++iterations;
        }
    }

    @Test public void cyclesThroughEnumValuesWhenMarkedWithValuesOf() {
        assertThat(testResult(EnumWithValuesOf.class), isSuccessful());
        assertEquals(repeat(asList(E1, E2, E3, E4, E5), 20), EnumWithValuesOf.values);
    }

    @RunWith(JUnitQuickcheck.class)
    public static class EnumWithValuesOf {
        static List<TestEnum> values = new ArrayList<>();

        @Property public void shouldHold(@ValuesOf TestEnum e) {
            values.add(e);
        }
    }

    @Test public void whenConstrained() throws Exception {
        assertThat(testResult(EnumWithConstraint.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), EnumWithConstraint.values.size());
        assertThat(EnumWithConstraint.values, not(hasItem(E3)));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class EnumWithConstraint {
        static List<TestEnum> values = new ArrayList<>();

        @Property public void shouldHold(
            @When(satisfies = "#_ != @com.pholser.junit.quickcheck.EnumPropertyParameterTypesTest$TestEnum@E3")
            TestEnum e) {

            values.add(e);
        }
    }

    @Test public void whenCyclingAndConstrained() {
        assertThat(testResult(EnumWithValuesOfAndConstraint.class), isSuccessful());
        assertEquals(repeat(asList(E1, E2, E4, E5), 25), EnumWithValuesOfAndConstraint.values);
    }

    @RunWith(JUnitQuickcheck.class)
    public static class EnumWithValuesOfAndConstraint {
        static List<TestEnum> values = new ArrayList<>();

        @Property public void shouldHold(
            @When(satisfies = "#_ != @com.pholser.junit.quickcheck.EnumPropertyParameterTypesTest$TestEnum@E3")
            @ValuesOf
            TestEnum e) {

            values.add(e);
        }
    }
}

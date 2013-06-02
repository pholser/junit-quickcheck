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

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.*;

import com.pholser.junit.quickcheck.generator.ValuesOf;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Annotations.*;
import static com.pholser.junit.quickcheck.ForAllEnumTheoryParameterTypesTest.TestEnum.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ForAllEnumTheoryParameterTypesTest {
    public enum TestEnum {
        E1, E2, E3, E4, E5
    }

    @Test public void usesRegularSampleSize() throws Exception {
        assertThat(testResult(EnumSuperclass.class), isSuccessful());
        assertEquals(defaultSampleSize(), EnumSuperclass.iterations);
    }

    @RunWith(Theories.class)
    public static class EnumSuperclass {
        static int iterations;

        @Theory public void shouldHold(@ForAll TestEnum e) {
            ++iterations;
        }
    }

    @Test public void usesNumberOfEnumsAsSampleSizeWhenMarkedWithValuesOf() {
        assertThat(testResult(EnumWithValuesOf.class), isSuccessful());
        assertEquals(asList(E1, E2, E3, E4, E5), EnumWithValuesOf.values);
    }

    @RunWith(Theories.class)
    public static class EnumWithValuesOf {
        static List<TestEnum> values = new ArrayList<TestEnum>();

        @Theory public void shouldHold(@ForAll @ValuesOf TestEnum e) {
            values.add(e);
        }
    }

    @Test public void whenConstrained() {
        assertThat(testResult(EnumWithValuesOfAndConstraint.class), isSuccessful());
        assertEquals(asList(E1, E2, E4, E5, E1), EnumWithValuesOfAndConstraint.values);
    }

    @RunWith(Theories.class)
    public static class EnumWithValuesOfAndConstraint {
        static List<TestEnum> values = new ArrayList<TestEnum>();

        @Theory public void shouldHold(
            @ForAll @ValuesOf
            @SuchThat("#_ != @com.pholser.junit.quickcheck.ForAllEnumTheoryParameterTypesTest$TestEnum@E3")
            TestEnum e) {

            values.add(e);
        }
    }
}

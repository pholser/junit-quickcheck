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

package com.pholser.junit.quickcheck;

import com.pholser.junit.quickcheck.test.generator.Between;
import com.pholser.junit.quickcheck.test.generator.Box;
import com.pholser.junit.quickcheck.test.generator.Foo;
import com.pholser.junit.quickcheck.test.generator.Mark;
import com.pholser.junit.quickcheck.test.generator.Pair;
import com.pholser.junit.quickcheck.test.generator.TestIntegerGenerator;
import org.junit.Test;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.test.generator.FooGenerator.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class MarkingTypeUsesWithConfigurationTest {
    @Test public void singleGenericParameterConfigured() throws Exception {
        assertThat(testResult(SingleGenericParameterConfigured.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class SingleGenericParameterConfigured {
        @Theory public void holds(@ForAll Box<@Same(-1) Foo> box) {
            assertEquals(-1, box.contents().i());
            assertFalse(box.marked());
            assertFalse(box.contents().marked());
        }
    }

    @Test public void parameterConfiguredButGenericParameterNotConfigured() throws Exception {
        assertThat(testResult(ParameterConfiguredButGenericParameterNotConfigured.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class ParameterConfiguredButGenericParameterNotConfigured {
        @Theory public void holds(@ForAll @Mark Box<Foo> box) {
            assertTrue(box.marked());
            assertFalse(box.contents().marked());
        }
    }

    @Test public void oneGenericParameterConfiguredButTheOtherNot() throws Exception {
        assertThat(testResult(OneGenericParameterConfiguredButTheOtherNot.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class OneGenericParameterConfiguredButTheOtherNot {
        @Theory public void holds(@ForAll Pair<Foo, @Mark Foo> pair) {
            assertFalse(pair.marked());
            assertFalse(pair.first().marked());
            assertTrue(pair.second().marked());
        }
    }

    @Test public void twoGenericParametersOfSameTypeConfiguredDifferently() throws Exception {
        assertThat(testResult(TwoGenericParametersOfSameTypeConfiguredDifferently.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class TwoGenericParametersOfSameTypeConfiguredDifferently {
        @Theory public void holds(@ForAll Pair<@Mark Box<@Same(1) Foo>, Box<@Mark @Same(2) Foo>> pair) {
            assertFalse(pair.marked());

            assertTrue(pair.first().marked());
            assertFalse(pair.first().contents().marked());
            assertEquals(1, pair.first().contents().i());

            assertFalse(pair.second().marked());
            assertTrue(pair.second().contents().marked());
            assertEquals(2, pair.second().contents().i());
        }
    }

    @Test public void twoGenericParametersReferringToSameType() throws Exception {
        assertThat(testResult(TwoGenericParametersReferringToSameType.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class TwoGenericParametersReferringToSameType {
        @Theory public void holds(@ForAll Pair<@Mark @Same(1) Foo, Box<@Mark @Same(2) Foo>> pair) {
            assertFalse(pair.marked());

            assertTrue(pair.first().marked());
            assertEquals(1, pair.first().i());

            assertFalse(pair.second().marked());
            assertTrue(pair.second().contents().marked());
            assertEquals(2, pair.second().contents().i());
        }
    }
}

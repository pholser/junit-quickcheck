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
import static com.pholser.junit.quickcheck.EnumPropertyParameterTypesTest.TestEnum.E3;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

public class EnumPropertyParameterTypesTest {
    public enum TestEnum {
        E1, E2, E3, E4, E5
    }

    @Test public void usesRegularTrialCount() throws Exception {
        assertThat(testResult(EnumTester.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), EnumTester.iterations);
        EnumTester.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class EnumTester {
        static int iterations;

        @Property public void shouldHold(TestEnum e) {
            ++iterations;
        }
    }

    @Test public void whenConstrained() throws Exception {
        assertThat(testResult(EnumWithConstraint.class), isSuccessful());
        assertEquals(
            defaultPropertyTrialCount(),
            EnumWithConstraint.values.size());
        assertThat(EnumWithConstraint.values, not(hasItem(E3)));
        EnumWithConstraint.values.clear();
    }

    @RunWith(JUnitQuickcheck.class)
    public static class EnumWithConstraint {
        static final List<TestEnum> values = new ArrayList<>();

        @Property public void shouldHold(
            @When(satisfies = "#_ != @com.pholser.junit.quickcheck.EnumPropertyParameterTypesTest$TestEnum@E3")
            TestEnum e) {

            values.add(e);
        }
    }
}

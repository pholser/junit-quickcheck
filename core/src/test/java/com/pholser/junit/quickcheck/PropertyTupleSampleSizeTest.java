/*
 The MIT License

 Copyright (c) 2010-2017 Paul R. Holser, Jr.

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

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.Foo;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Annotations.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class PropertyTupleSampleSizeTest {
    @Test public void shouldFeedADefaultNumberOfValuesToAProperty() throws Exception {
        assertThat(testResult(ForDefaultNumberOfValues.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), ForDefaultNumberOfValues.iterations);
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ForDefaultNumberOfValues {
        static int iterations;

        @Property public void shouldHold(Foo f) {
            ++iterations;
        }
    }

    @Test public void shouldRespectTrialCountIfSpecified() {
        assertThat(testResult(ForSpecifiedNumberOfValues.class), isSuccessful());
        assertEquals(5, ForSpecifiedNumberOfValues.iterations);
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ForSpecifiedNumberOfValues {
        static int iterations;

        @Property(trials = 5) public void shouldHold(Foo f) {
            ++iterations;
        }
    }

    @Test public void trialCountHoldsForEntirePropertyRatherThanIndividualParameters() {
        assertThat(testResult(ForValuesOfMultipleParameters.class), isSuccessful());
        assertEquals(21, ForValuesOfMultipleParameters.iterations);
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ForValuesOfMultipleParameters {
        static int iterations;

        @Property(trials = 21) public void shouldHold(Foo f, Foo g) {
            ++iterations;
        }
    }
}

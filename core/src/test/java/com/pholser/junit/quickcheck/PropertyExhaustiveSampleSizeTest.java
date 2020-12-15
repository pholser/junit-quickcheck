/*
 The MIT License

 Copyright (c) 2010-2020 Paul R. Holser, Jr.

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
import static com.pholser.junit.quickcheck.Mode.EXHAUSTIVE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.Foo;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

public class PropertyExhaustiveSampleSizeTest {
    @Test public void shouldFeedADefaultNumberOfValuesToAProperty()
        throws Exception {

        assertThat(
            testResult(ForDefaultNumberOfValues.class),
            isSuccessful());
        assertEquals(
            defaultPropertyTrialCount(),
            ForDefaultNumberOfValues.iterations);
        ForDefaultNumberOfValues.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ForDefaultNumberOfValues {
        static int iterations;

        @Property(mode = EXHAUSTIVE) public void shouldHold(Foo f) {
            ++iterations;
        }
    }

    @Test public void shouldRespectTrialCountIfSpecified() {
        assertThat(
            testResult(ForSpecifiedNumberOfValues.class),
            isSuccessful());
        assertEquals(5, ForSpecifiedNumberOfValues.iterations);
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ForSpecifiedNumberOfValues {
        static int iterations;

        @Property(trials = 5, mode = EXHAUSTIVE)
        public void shouldHold(Foo f) {
            ++iterations;
        }
    }

    @Test public void
    trialCountHoldsForEntirePropertyRatherThanIndividualParameters() {
        assertThat(
            testResult(ForValuesOfMultipleParameters.class),
            isSuccessful());

        assertEquals(4, ForValuesOfMultipleParameters.iterations);
        List<Foo> foos = ForValuesOfMultipleParameters.foos;
        assertEquals(8, foos.size());
        assertEquals(foos.get(0), foos.get(4));
        assertEquals(foos.get(1), foos.get(3));
        assertEquals(foos.get(2), foos.get(6));
        assertEquals(foos.get(5), foos.get(7));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ForValuesOfMultipleParameters {
        static int iterations;
        static final List<Foo> foos = new ArrayList<>();

        @Property(trials = 2, mode = EXHAUSTIVE)
        public void shouldHold(Foo f, Foo g) {
            ++iterations;
            foos.add(f);
            foos.add(g);
        }
    }
}

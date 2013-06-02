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

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Annotations.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ForAllSampleSizeTest {
    @Test public void shouldFeedADefaultNumberOfValuesToAMarkedParameter() throws Exception {
        assertThat(testResult(ForDefaultNumberOfValues.class), isSuccessful());
        assertEquals(defaultSampleSize(), ForDefaultNumberOfValues.iterations);
    }

    @RunWith(Theories.class)
    public static class ForDefaultNumberOfValues {
        static int iterations;

        @Theory public void shouldHold(@ForAll int i) {
            ++iterations;
        }
    }

    @Test public void shouldRespectSampleSizeIfSpecified() {
        assertThat(testResult(ForSpecifiedNumberOfValues.class), isSuccessful());
        assertEquals(5, ForSpecifiedNumberOfValues.iterations);
    }

    @RunWith(Theories.class)
    public static class ForSpecifiedNumberOfValues {
        static int iterations;

        @Theory public void shouldHold(@ForAll(sampleSize = 5) int i) {
            ++iterations;
        }
    }

    @Test public void shouldBeAbleToMarkMultipleParametersForReceivingValues() {
        assertThat(testResult(ForValuesOfMultipleParameters.class), isSuccessful());
        assertEquals(21, ForValuesOfMultipleParameters.iterations);
    }

    @RunWith(Theories.class)
    public static class ForValuesOfMultipleParameters {
        static int iterations;

        @Theory public void shouldHold(@ForAll(sampleSize = 3) int i, @ForAll(sampleSize = 7) int j) {
            ++iterations;
        }
    }
}

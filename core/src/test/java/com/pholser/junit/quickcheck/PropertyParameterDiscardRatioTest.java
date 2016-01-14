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

import com.pholser.junit.quickcheck.internal.generator.PropertyParameterGenerationContext.DiscardRatioExceededException;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.Foo;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class PropertyParameterDiscardRatioTest {
    @Test public void negativeRatioWillNeverGenerateValues() {
        assertThat(
            testResult(NegativeDiscardRatio.class),
            hasFailureContaining(IllegalArgumentException.class.getName()));
        assertEquals(0, NegativeDiscardRatio.iterations);
    }

    @RunWith(JUnitQuickcheck.class)
    public static class NegativeDiscardRatio {
        static int iterations;

        @Property public void shouldHold(@When(discardRatio = -1) Foo f) {
            ++iterations;
        }
    }

    @Test public void willStopGeneratingValuesAfterDiscardRatioExceeded() {
        assertThat(
            testResult(ExceededDiscardRatio.class),
            hasFailureContaining(DiscardRatioExceededException.class.getName()));
        assertEquals(0, ExceededDiscardRatio.iterations);
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ExceededDiscardRatio {
        static int iterations;

        @Property public void shouldHold(@When(discardRatio = 3, satisfies = "false") Foo f) {
            ++iterations;
        }
    }

    @Test public void zeroRatioStopsAfterDiscardsExceedSampleSize() {
        assertThat(
            testResult(ZeroDiscardRatio.class),
            hasFailureContaining(DiscardRatioExceededException.class.getName()));
        assertEquals(0, ZeroDiscardRatio.iterations);
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ZeroDiscardRatio {
        static int iterations;

        @Property public void shouldHold(@When(satisfies = "false") Foo f) {
            ++iterations;
        }
    }
}

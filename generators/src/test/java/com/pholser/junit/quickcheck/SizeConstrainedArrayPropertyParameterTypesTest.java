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

import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class SizeConstrainedArrayPropertyParameterTypesTest {
    @Test public void oneD() {
        assertThat(testResult(OneD.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class OneD {
        @Property public void shouldHold(boolean @Size(min = 2, max = 4) [] b) {
            assertThat(b.length, allOf(greaterThanOrEqualTo(2), lessThanOrEqualTo(4)));
        }
    }

    @Test public void twoD() {
        assertThat(testResult(TwoD.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class TwoD {
        @Property public void shouldHold(boolean @Size(min = 13, max = 15) [] @Size(max = 2) [] b) {
            assertThat(b.length, allOf(greaterThanOrEqualTo(13), lessThanOrEqualTo(15)));
            for (boolean[] each : b)
                assertThat(each.length, lessThanOrEqualTo(2));
        }
    }

    @Test public void shrinkingTwoD() {
        assertThat(testResult(ShrinkingTwoD.class), failureCountIs(1));
        assertThat(
            ShrinkingTwoD.failed.length,
            allOf(greaterThanOrEqualTo(13), lessThanOrEqualTo(15)));
        for (int[] each : ShrinkingTwoD.failed)
            assertThat(each.length, lessThanOrEqualTo(2));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingTwoD {
        static int[][] failed;

        @Property public void shouldHold(int @Size(min = 13, max = 15) [] @Size(max = 2) [] i) {
            failed = i;

            fail();
        }
    }

    @Test public void threeD() {
        assertThat(testResult(ThreeD.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ThreeD {
        @Property public void shouldHold(boolean @Size(max = 6) [] @Size(max = 4) [] @Size(max = 2) [] z) {
            assertThat(z.length, lessThanOrEqualTo(6));
            for (boolean[][] y : z) {
                assertThat(y.length, lessThanOrEqualTo(4));
                for (boolean[] x : y)
                    assertThat(x.length, lessThanOrEqualTo(2));
            }
        }
    }

    @Test public void outOfWhackSizeRange() {
        assertThat(
            testResult(OutOfWhackSizeRange.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class OutOfWhackSizeRange {
        @Property public void shouldHold(boolean @Size(min = 2, max = 1) [] b) {
            assertThat(b.length, allOf(greaterThanOrEqualTo(2), lessThanOrEqualTo(1)));
        }
    }
}

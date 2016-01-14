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

import java.util.Map;

import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class SizeConstrainedMapPropertyParameterTypesTest {
    @Test public void sizeConstrainedMaps() {
        assertThat(testResult(SizeConstrainedMaps.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class SizeConstrainedMaps {
        @Property public void shouldHold(@Size(min = 2, max = 5) Map<String, Double> items) {
            assertThat(items.size(), lessThanOrEqualTo(5));
        }
    }

    @Test public void shrinkingSizeConstrainedMaps() {
        assertThat(testResult(ShrinkingSizeConstrainedMaps.class), failureCountIs(1));
        assertThat(
            ShrinkingSizeConstrainedMaps.failed.size(),
            allOf(greaterThanOrEqualTo(2), lessThanOrEqualTo(5)));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingSizeConstrainedMaps {
        static Map<Integer, Double> failed;

        @Property public void shouldHold(@Size(min = 2, max = 5) Map<Integer, Double> items) {
            failed = items;

            fail();
        }
    }

    @Test public void outOfWhackSizeRange() {
        assertThat(
            testResult(OutOfWhackSizeRange.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class OutOfWhackSizeRange {
        @Property public void shouldHold(@Size(min = 4, max = 3) Map<Integer, ?> items) {
            assertThat(items.size(), lessThanOrEqualTo(3));
        }
    }
}

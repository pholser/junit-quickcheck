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

import com.pholser.junit.quickcheck.generator.Distinct;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class DistinctMapPropertyParameterTypesTest {
    @Test public void distinctMaps() {
        assertThat(testResult(DistinctMaps.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class DistinctMaps {
        @Property public void shouldHold(
            @Size(min = 4, max = 5)
            @Distinct
            Map<@InRange(minInt = 1, maxInt = 5) Integer, Double> items) {

            assertThat(
                items.size(),
                allOf(
                    greaterThanOrEqualTo(4),
                    lessThanOrEqualTo(5)));
        }
    }

    @Test public void shrinkingDistinctMaps() {
        assertThat(testResult(ShrinkingDistinctMaps.class), failureCountIs(1));
        assertThat(
            ShrinkingDistinctMaps.failed.size(),
            allOf(
                greaterThanOrEqualTo(4),
                lessThanOrEqualTo(5)));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingDistinctMaps {
        static Map<Integer, Double> failed;

        @Property public void shouldHold(
            @Size(min = 4, max = 5)
            @Distinct
            Map<@InRange(minInt = 1, maxInt = 5) Integer, Double> items) {

            failed = items;

            fail();
        }
    }
}

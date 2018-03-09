/*
 The MIT License

 Copyright (c) 2010-2018 Paul R. Holser, Jr.

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
import com.pholser.junit.quickcheck.internal.Lists;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class DistinctListPropertyParameterTypesTest {
    @Test public void distinctLists() {
        assertThat(testResult(DistinctLists.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class DistinctLists {
        @Property public void shouldHold(
            @Size(min = 2, max = 5)
            @Distinct
            List<@InRange(minInt = 1, maxInt = 5) Integer> items) {

            assertThat(
                items.size(),
                allOf(
                    greaterThanOrEqualTo(2),
                    lessThanOrEqualTo(5)));
            assertTrue(Lists.isDistinct(items));
        }
    }

    @Test public void shrinkingDistinctLists() {
        assertThat(
            testResult(ShrinkingDistinctLists.class),
            failureCountIs(1));
        assertThat(
            ShrinkingDistinctLists.failed.size(),
            allOf(
                greaterThanOrEqualTo(4),
                lessThanOrEqualTo(5)));
        assertTrue(Lists.isDistinct(ShrinkingDistinctLists.failed));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingDistinctLists {
        static List<Integer> failed;

        @Property public void shouldHold(
            @Size(min = 4, max = 5)
            @Distinct
            List<@InRange(minInt = 1, maxInt = 5) Integer> items) {

            failed = items;

            fail();
        }
    }
}

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

import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.ZoneOffset;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.hasSingleFailureContaining;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

public class ZoneOffsetPropertyParameterTypesTest {
    @Test public void zoneOffset() {
        assertThat(testResult(ZoneOffsetTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ZoneOffsetTheory {
        @Property public void shouldHold(ZoneOffset d) {
        }
    }

    @Test public void rangedZoneOffset() {
        assertThat(testResult(RangedZoneOffsetTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedZoneOffsetTheory {
        @Property public void shouldHold(
            @InRange(min = "+10:00", max = "-18:00") ZoneOffset d) throws Exception {

            assertThat(
                d,
                allOf(
                    greaterThanOrEqualTo(ZoneOffset.of("+10:00")),
                    lessThanOrEqualTo(ZoneOffset.of("-18:00"))));
        }
    }

    @Test public void malformedMin() {
        assertThat(
            testResult(MalformedMinZoneOffsetTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMinZoneOffsetTheory {
        @Property public void shouldHold(
            @InRange(min = "@#!@#@", max = "-12:00") ZoneOffset d) {
        }
    }

    @Test public void malformedMax() {
        assertThat(
            testResult(MalformedMaxZoneOffsetTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMaxZoneOffsetTheory {
        @Property public void shouldHold(
            @InRange(min = "+12:00", max = "*&@^#%$") ZoneOffset d) {
        }
    }

    @Test public void missingMin() {
        assertThat(testResult(MissingMinTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMinTheory {
        @Property public void shouldHold(@InRange(max = "-12:00") ZoneOffset d) throws Exception {
            assertThat(d, lessThanOrEqualTo(ZoneOffset.of("-12:00")));
        }
    }

    @Test public void missingMax() {
        assertThat(testResult(MissingMaxTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMaxTheory {
        @Property public void shouldHold(@InRange(min = "+12:00") ZoneOffset d) throws Exception {
            assertThat(d, greaterThanOrEqualTo(ZoneOffset.of("+12:00")));
        }
    }

    @Test public void backwardsRange() {
        assertThat(
            testResult(BackwardsRangeTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class BackwardsRangeTheory {
        @Property public void shouldHold(
            @InRange(min = "-12:00", max = "+12:00") ZoneOffset d) {
        }
    }
}

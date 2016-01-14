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

import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.DateTimeException;
import java.time.ZoneOffset;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.hasSingleFailureContaining;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

public class ZoneOffsetPropertyParameterTypesTest {
    @Test public void zoneOffset() {
        assertThat(testResult(ZoneOffsets.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ZoneOffsets {
        @Property public void shouldHold(ZoneOffset o) {
        }
    }

    @Test public void rangedZoneOffset() {
        assertThat(testResult(RangedZoneOffset.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedZoneOffset {
        @Property public void shouldHold(
            @InRange(min = "+10:00", max = "-18:00") ZoneOffset o) {

            assertThat(
                o,
                allOf(
                    greaterThanOrEqualTo(ZoneOffset.of("+10:00")),
                    lessThanOrEqualTo(ZoneOffset.of("-18:00"))));
        }
    }

    @Test public void malformedMin() {
        assertThat(
            testResult(MalformedMinZoneOffset.class),
            hasSingleFailureContaining(DateTimeException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMinZoneOffset {
        @Property public void shouldHold(
            @InRange(min = "@#!@#@", max = "-12:00") ZoneOffset o) {
        }
    }

    @Test public void malformedMax() {
        assertThat(
            testResult(MalformedMaxZoneOffset.class),
            hasSingleFailureContaining(DateTimeException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMaxZoneOffset {
        @Property public void shouldHold(
            @InRange(min = "+12:00", max = "*&@^#%$") ZoneOffset o) {
        }
    }

    @Test public void missingMin() {
        assertThat(testResult(MissingMin.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMin {
        @Property public void shouldHold(@InRange(max = "-12:00") ZoneOffset o) {
            assertThat(o, lessThanOrEqualTo(ZoneOffset.of("-12:00")));
        }
    }

    @Test public void missingMax() {
        assertThat(testResult(MissingMax.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMax {
        @Property public void shouldHold(@InRange(min = "+12:00") ZoneOffset o) {
            assertThat(o, greaterThanOrEqualTo(ZoneOffset.of("+12:00")));
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
            @InRange(min = "-12:00", max = "+12:00") ZoneOffset o) {
        }
    }
}

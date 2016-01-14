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

import java.time.Clock;
import java.time.Instant;
import java.time.format.DateTimeParseException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.hasSingleFailureContaining;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

public class ClockPropertyParameterTypesTest {
    @Test public void clock() {
        assertThat(testResult(Clocks.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Clocks {
        @Property public void shouldHold(Clock c) {
        }
    }

    @Test public void rangedClock() {
        assertThat(testResult(RangedClock.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedClock {
        @Property public void shouldHold(
            @InRange(
                min = "2012-01-01T00:00:00.0Z",
                max = "2012-12-31T23:59:59.999999999Z")
            Clock c) {

            assertThat(
                c.instant(),
                allOf(
                    greaterThanOrEqualTo(Instant.parse("2012-01-01T00:00:00.0Z")),
                    lessThanOrEqualTo(Instant.parse("2012-12-31T23:59:59.999999999Z"))));
        }
    }

    @Test public void malformedMin() {
        assertThat(
            testResult(MalformedMinClock.class),
            hasSingleFailureContaining(DateTimeParseException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMinClock {
        @Property public void shouldHold(
            @InRange(
                min = "@#!@#@",
                max = "2012-12-31T23:59:59.999999999Z")
            Clock c) {
        }
    }

    @Test public void malformedMax() {
        assertThat(
            testResult(MalformedMaxClock.class),
            hasSingleFailureContaining(DateTimeParseException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMaxClock {
        @Property public void shouldHold(
            @InRange(
                min = "06/01/2011T23:59:59.999999999Z",
                max = "*&@^#%$")
            Clock c) {
        }
    }

    @Test public void missingMin() {
        assertThat(testResult(MissingMin.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMin {
        @Property public void shouldHold(
            @InRange(max = "2012-12-31T23:59:59.999999999Z") Clock c) {

            assertThat(
                c.instant(),
                lessThanOrEqualTo(Instant.parse("2012-12-31T23:59:59.999999999Z")));
        }
    }

    @Test public void missingMax() {
        assertThat(testResult(MissingMax.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMax {
        @Property public void shouldHold(
            @InRange(min = "2012-12-31T23:59:59.999999999Z") Clock c) {

            assertThat(
                c.instant(),
                greaterThanOrEqualTo(Instant.parse("2012-12-31T23:59:59.999999999Z")));
        }
    }

    @Test public void backwardsRange() {
        assertThat(
            testResult(BackwardsRangeTheory.class),
            hasSingleFailureContaining(DateTimeParseException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class BackwardsRangeTheory {
        @Property public void shouldHold(
            @InRange(
                min = "2012-12-31T23:59:59.999999999Z",
                max = "12/01/2012T00:00:00.0Z")
            Clock c) {
        }
    }
}

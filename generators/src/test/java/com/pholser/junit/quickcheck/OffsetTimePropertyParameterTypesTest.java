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

import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.hasSingleFailureContaining;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

public class OffsetTimePropertyParameterTypesTest {
    @Test public void offsetTime() {
        assertThat(testResult(OffsetTimes.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class OffsetTimes {
        @Property public void shouldHold(OffsetTime t) {
        }
    }

    @Test public void rangedOffsetTime() {
        assertThat(testResult(RangedOffsetTime.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedOffsetTime {
        @Property public void shouldHold(
            @InRange(
                min = "00:00:00.0+00:00",
                max = "22:59:59.999999999+01:00",
                format = "HH:mm:ss.nxxx")
            OffsetTime t) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.nxxx");

            assertThat(
                t,
                allOf(
                    greaterThanOrEqualTo(OffsetTime.parse("00:00:00.0+01:00", formatter)),
                    lessThanOrEqualTo(OffsetTime.parse("23:59:59.999999999+01:00", formatter))));
        }
    }

    @Test public void malformedMin() {
        assertThat(
            testResult(MalformedMinOffsetTime.class),
            hasSingleFailureContaining(DateTimeParseException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMinOffsetTime {
        @Property public void shouldHold(
            @InRange(
                min = "@#!@#@",
                max = "13:45.30.123456789+01:00",
                format = "HH:mm:ss.nxxx")
            OffsetTime t) {
        }
    }

    @Test public void malformedMax() {
        assertThat(
            testResult(MalformedMaxOffsetTime.class),
            hasSingleFailureContaining(DateTimeParseException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMaxOffsetTime {
        @Property public void shouldHold(
            @InRange(
                min = "01:15:23.23929+01:00",
                max = "*&@^#%$",
                format = "HH:mm:ss.nxxx")
            OffsetTime t) {
        }
    }

    @Test public void malformedFormat() {
        assertThat(
            testResult(MalformedFormatOffsetTime.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedFormatOffsetTime {
        @Property public void shouldHold(
            @InRange(
                min = "00:00:00.0+00:00",
                max = "22:59:59.999999999+01:00",
                format = "*@&^#$")
            OffsetTime t) {
        }
    }

    @Test public void missingMin() {
        assertThat(testResult(MissingMin.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMin {
        @Property public void shouldHold(
            @InRange(
                max = "22:59:59.999999999+01:00",
                format = "HH:mm:ss.nxxx")
            OffsetTime t) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.nxxx");
            assertThat(
                t,
                lessThanOrEqualTo(
                    OffsetTime.parse("23:59:59.999999999+01:00", formatter)));
        }
    }

    @Test public void missingMax() {
        assertThat(testResult(MissingMax.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMax {
        @Property public void shouldHold(
            @InRange(
                min = "00:00:00.0+00:00",
                format = "HH:mm:ss.nxxx")
            OffsetTime t) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.nxxx");
            assertThat(
                t,
                greaterThanOrEqualTo(OffsetTime.parse("00:00:00.0+00:00", formatter)));
        }
    }

    @Test public void backwardsRange() {
        assertThat(
            testResult(BackwardsRange.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class BackwardsRange {
        @Property public void shouldHold(
            @InRange(
                min = "22:59:59.999999999+01:00",
                max = "00:00:00.0+00:00",
                format = "HH:mm:ss.nxxx")
            OffsetTime t) {
        }
    }
}

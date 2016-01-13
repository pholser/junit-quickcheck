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

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.hasSingleFailureContaining;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

public class ZonedDateTimePropertyParameterTypesTest {
    @Test
    public void zonedDateTime() {
        assertThat(testResult(ZonedDateTimeTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ZonedDateTimeTheory {
        @Property
        public void shouldHold(ZonedDateTime d) {
        }
    }

    @Test
    public void rangedZonedDateTime() {
        assertThat(testResult(RangedZonedDateTimeTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedZonedDateTimeTheory {
        @Property
        public void shouldHold(
            @InRange(min = "01/01/2012T00:00:00.0+01:00[America/Chicago]", max = "12/31/2012T23:59:59.999999999+01:00[America/Chicago]",
                format = "MM/dd/yyyy'T'HH:mm:ss.nxxx'['VV']'") ZonedDateTime d) throws Exception {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy'T'HH:mm:ss.nxxx'['VV']'");

            assertThat(
                d,
                allOf(
                    greaterThanOrEqualTo(ZonedDateTime.parse("01/01/2012T00:00:00.0+01:00[America/Chicago]", formatter)),
                    lessThanOrEqualTo(ZonedDateTime.parse("12/31/2012T23:59:59.999999999+01:00[America/Chicago]", formatter))));
        }
    }

    @Test
    public void malformedMin() {
        assertThat(
            testResult(MalformedMinZonedDateTimeTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMinZonedDateTimeTheory {
        @Property
        public void shouldHold(
            @InRange(min = "@#!@#@", max = "12/31/2012T23:59:59.999999999+01:00[America/Chicago]",
                format = "MM/dd/yyyy'T'HH:mm:ss.nxxx'['VV']'") ZonedDateTime d) {
        }
    }

    @Test
    public void malformedMax() {
        assertThat(
            testResult(MalformedMaxZonedDateTimeTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMaxZonedDateTimeTheory {
        @Property
        public void shouldHold(
            @InRange(min = "06/01/2011T23:59:59.999999999+01:00[America/Chicago]", max = "*&@^#%$",
                format = "MM/dd/yyyy'T'HH:mm:ss.nxxx'['VV']'") ZonedDateTime d) {
        }
    }

    @Test
    public void malformedFormat() {
        assertThat(
            testResult(MalformedFormatZonedDateTimeTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedFormatZonedDateTimeTheory {
        @Property
        public void shouldHold(
            @InRange(min = "06/01/2011T23:59:59.999999999+01:00[America/Chicago]", max = "06/30/2011T23:59:59.999999999+01:00[America/Chicago]",
                format = "*@&^#$") ZonedDateTime d) {
        }
    }

    @Test
    public void missingMin() {
        assertThat(testResult(MissingMinTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMinTheory {
        @Property
        public void shouldHold(
            @InRange(max = "12/31/2012T23:59:59.999999999+01:00[America/Chicago]", format = "MM/dd/yyyy'T'HH:mm:ss.nxxx'['VV']'")
            ZonedDateTime d)
            throws Exception {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy'T'HH:mm:ss.nxxx'['VV']'");
            assertThat(d, lessThanOrEqualTo(ZonedDateTime.parse("12/31/2012T23:59:59.999999999+01:00[America/Chicago]", formatter)));
        }
    }

    @Test
    public void missingMax() {
        assertThat(testResult(MissingMaxTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMaxTheory {
        @Property
        public void shouldHold(
            @InRange(min = "12/31/2012T23:59:59.999999999+01:00[America/Chicago]", format = "MM/dd/yyyy'T'HH:mm:ss.nxxx'['VV']'")
            ZonedDateTime d)
            throws Exception {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy'T'HH:mm:ss.nxxx'['VV']'");
            assertThat(d, greaterThanOrEqualTo(ZonedDateTime.parse("12/31/2012T23:59:59.999999999+01:00[America/Chicago]", formatter)));
        }
    }

    @Test
    public void backwardsRange() {
        assertThat(
            testResult(BackwardsRangeTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class BackwardsRangeTheory {
        @Property
        public void shouldHold(
            @InRange(min = "12/31/2012T23:59:59.999999999+01:00[America/Chicago]", max = "12/01/2012T00:00:00.0+01:00[America/Chicago]",
                format = "MM/dd/yyyy'T'HH:mm:ss.nxxx'['VV']'") ZonedDateTime d) {
        }
    }
}

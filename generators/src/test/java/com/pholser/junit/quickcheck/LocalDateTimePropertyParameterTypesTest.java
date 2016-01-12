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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.hasSingleFailureContaining;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

public class LocalDateTimePropertyParameterTypesTest {
    @Test
    public void localDateTime() {
        assertThat(testResult(LocalDateTimeTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class LocalDateTimeTheory {
        @Property
        public void shouldHold(LocalDateTime d) {
        }
    }

    @Test
    public void rangedLocalDateTime() {
        assertThat(testResult(RangedLocalDateTimeTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedLocalDateTimeTheory {
        @Property
        public void shouldHold(
            @InRange(min = "01/01/2012T00:00:00.0", max = "12/31/2012T23:59:59.999999999",
                format = "MM/dd/yyyy'T'HH:mm:ss.n") LocalDateTime d) throws Exception {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy'T'HH:mm:ss.n");

            assertThat(
                d,
                allOf(
                    greaterThanOrEqualTo(LocalDateTime.parse("01/01/2012T00:00:00.0", formatter)),
                    lessThanOrEqualTo(LocalDateTime.parse("12/31/2012T23:59:59.999999999", formatter))));
        }
    }

    @Test
    public void malformedMin() {
        assertThat(
            testResult(MalformedMinLocalDateTimeTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMinLocalDateTimeTheory {
        @Property
        public void shouldHold(
            @InRange(min = "@#!@#@", max = "12/31/2012T23:59:59.999999999",
                format = "MM/dd/yyyy'T'HH:mm:ss.n") LocalDateTime d) {
        }
    }

    @Test
    public void malformedMax() {
        assertThat(
            testResult(MalformedMaxLocalDateTimeTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMaxLocalDateTimeTheory {
        @Property
        public void shouldHold(
            @InRange(min = "06/01/2011T23:59:59.999999999", max = "*&@^#%$",
                format = "MM/dd/yyyy'T'HH:mm:ss.n") LocalDateTime d) {
        }
    }

    @Test
    public void malformedFormat() {
        assertThat(
            testResult(MalformedFormatLocalDateTimeTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedFormatLocalDateTimeTheory {
        @Property
        public void shouldHold(
            @InRange(min = "06/01/2011T23:59:59.999999999", max = "06/30/2011T23:59:59.999999999",
                format = "*@&^#$") LocalDateTime d) {
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
            @InRange(max = "12/31/2012T23:59:59.999999999", format = "MM/dd/yyyy'T'HH:mm:ss.n") LocalDateTime d)
            throws Exception {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy'T'HH:mm:ss.n");
            assertThat(d, lessThanOrEqualTo(LocalDateTime.parse("12/31/2012T23:59:59.999999999", formatter)));
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
            @InRange(min = "12/31/2012T23:59:59.999999999", format = "MM/dd/yyyy'T'HH:mm:ss.n") LocalDateTime d)
            throws Exception {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy'T'HH:mm:ss.n");
            assertThat(d, greaterThanOrEqualTo(LocalDateTime.parse("12/31/2012T23:59:59.999999999", formatter)));
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
            @InRange(min = "12/31/2012T23:59:59.999999999", max = "12/01/2012T00:00:00.0",
                format = "MM/dd/yyyy'T'HH:mm:ss.n") LocalDateTime d) {
        }
    }
}

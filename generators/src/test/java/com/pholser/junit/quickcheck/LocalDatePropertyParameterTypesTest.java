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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.hasSingleFailureContaining;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

public class LocalDatePropertyParameterTypesTest {
    @Test public void localDate() {
        assertThat(testResult(LocalDates.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class LocalDates {
        @Property public void shouldHold(LocalDate d) {
        }
    }

    @Test public void rangedLocalDate() {
        assertThat(testResult(RangedLocalDate.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedLocalDate {
        @Property public void shouldHold(
            @InRange(
                min = "01/01/2012",
                max = "12/31/2012",
                format = "MM/dd/yyyy")
            LocalDate d) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

            assertThat(
                d,
                allOf(
                    greaterThanOrEqualTo(LocalDate.parse("01/01/2012", formatter)),
                    lessThanOrEqualTo(LocalDate.parse("12/31/2012", formatter))));
        }
    }

    @Test public void malformedMin() {
        assertThat(
            testResult(MalformedMinLocalDate.class),
            hasSingleFailureContaining(DateTimeParseException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMinLocalDate {
        @Property public void shouldHold(
            @InRange(
                min = "@#!@#@",
                max = "12/31/2012",
                format = "MM/dd/yyyy")
            LocalDate d) {
        }
    }

    @Test public void malformedMax() {
        assertThat(
            testResult(MalformedMaxLocalDate.class),
            hasSingleFailureContaining(DateTimeParseException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMaxLocalDate {
        @Property public void shouldHold(
            @InRange(
                min = "06/01/2011",
                max = "*&@^#%$",
                format = "MM/dd/yyyy")
            LocalDate d) {
        }
    }

    @Test public void malformedFormat() {
        assertThat(
            testResult(MalformedFormatLocalDate.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedFormatLocalDate {
        @Property public void shouldHold(
            @InRange(
                min = "06/01/2011",
                max = "06/30/2011",
                format = "*@&^#$")
            LocalDate d) {
        }
    }

    @Test public void missingMin() {
        assertThat(testResult(MissingMinTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMinTheory {
        @Property public void shouldHold(
            @InRange(max = "12/31/2012", format = "MM/dd/yyyy") LocalDate d) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            assertThat(d, lessThanOrEqualTo(LocalDate.parse("12/31/2012", formatter)));
        }
    }

    @Test public void missingMax() {
        assertThat(testResult(MissingMax.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMax {
        @Property public void shouldHold(
            @InRange(min = "12/31/2012", format = "MM/dd/yyyy") LocalDate d) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            assertThat(d, greaterThanOrEqualTo(LocalDate.parse("12/31/2012", formatter)));
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
                min = "12/31/2012",
                max = "12/01/2012",
                format = "MM/dd/yyyy")
            LocalDate d) {
        }
    }
}

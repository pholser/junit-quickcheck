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

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.hasSingleFailureContaining;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

public class YearMonthPropertyParameterTypesTest {
    @Test public void yearMonth() {
        assertThat(testResult(YearMonths.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class YearMonths {
        @Property public void shouldHold(YearMonth m) {
        }
    }

    @Test public void rangedYearMonth() {
        assertThat(testResult(RangedYearMonth.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedYearMonth {
        @Property public void shouldHold(
            @InRange(min = "1776/04", max = "1976/12", format = "yyyy/MM") YearMonth m) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM");

            assertThat(
                m,
                allOf(
                    greaterThanOrEqualTo(YearMonth.parse("1776/04", formatter)),
                    lessThanOrEqualTo(YearMonth.parse("1976/12", formatter))));
        }
    }

    @Test public void malformedMin() {
        assertThat(
            testResult(MalformedMinYearMonth.class),
            hasSingleFailureContaining(DateTimeParseException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMinYearMonth {
        @Property public void shouldHold(
            @InRange(min = "@#!@#@", max = "1976/12", format = "yyyy/MM") YearMonth m) {
        }
    }

    @Test public void malformedMax() {
        assertThat(
            testResult(MalformedMaxYearMonth.class),
            hasSingleFailureContaining(DateTimeParseException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMaxYearMonth {
        @Property public void shouldHold(
            @InRange(min = "1006/01", max = "*&@^#%$", format = "yyyy/MM") YearMonth m) {
        }
    }

    @Test public void malformedFormat() {
        assertThat(
            testResult(MalformedFormatYearMonth.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedFormatYearMonth {
        @Property public void shouldHold(
            @InRange(min = "1006/01", max = "1006/03", format = "*@&^#$") YearMonth m) {
        }
    }

    @Test public void missingMin() {
        assertThat(testResult(MissingMin.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMin {
        @Property public void shouldHold(
            @InRange(max = "1976/12", format = "yyyy/MM") YearMonth m) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM");
            assertThat(m, lessThanOrEqualTo(YearMonth.parse("1976/12", formatter)));
        }
    }

    @Test public void missingMax() {
        assertThat(testResult(MissingMax.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMax {
        @Property public void shouldHold(
            @InRange(min = "1976/12", format = "yyyy/MM") YearMonth m) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM");
            assertThat(m, greaterThanOrEqualTo(YearMonth.parse("1976/12", formatter)));
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
            @InRange(min = "1976/12", max = "1012/01", format = "yyyy/MM") YearMonth m) {
        }
    }
}

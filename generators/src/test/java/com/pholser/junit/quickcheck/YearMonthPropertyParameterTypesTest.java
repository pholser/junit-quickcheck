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

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.hasSingleFailureContaining;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

public class YearMonthPropertyParameterTypesTest {
    @Test
    public void yearMonth() {
        assertThat(testResult(YearMonthTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class YearMonthTheory {
        @Property
        public void shouldHold(YearMonth d) {
        }
    }

    @Test
    public void rangedYearMonth() {
        assertThat(testResult(RangedYearMonthTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedYearMonthTheory {
        @Property
        public void shouldHold(
            @InRange(min = "1776/04", max = "1976/12", format = "yyyy/MM") YearMonth d) throws Exception {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM");

            assertThat(
                d,
                allOf(
                    greaterThanOrEqualTo(YearMonth.parse("1776/04", formatter)),
                    lessThanOrEqualTo(YearMonth.parse("1976/12", formatter))));
        }
    }

    @Test
    public void malformedMin() {
        assertThat(
            testResult(MalformedMinYearMonthTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMinYearMonthTheory {
        @Property
        public void shouldHold(
            @InRange(min = "@#!@#@", max = "1976/12", format = "yyyy/MM") YearMonth d) {
        }
    }

    @Test
    public void malformedMax() {
        assertThat(
            testResult(MalformedMaxYearMonthTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMaxYearMonthTheory {
        @Property
        public void shouldHold(
            @InRange(min = "1006/01", max = "*&@^#%$", format = "yyyy/MM") YearMonth d) {
        }
    }

    @Test
    public void malformedFormat() {
        assertThat(
            testResult(MalformedFormatYearMonthTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedFormatYearMonthTheory {
        @Property
        public void shouldHold(
            @InRange(min = "1006/01", max = "1006/03", format = "*@&^#$") YearMonth d) {
        }
    }

    @Test
    public void missingMin() {
        assertThat(testResult(MissingMinTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMinTheory {
        @Property
        public void shouldHold(@InRange(max = "1976/12", format = "yyyy/MM") YearMonth d)
            throws Exception {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM");
            assertThat(d, lessThanOrEqualTo(YearMonth.parse("1976/12", formatter)));
        }
    }

    @Test
    public void missingMax() {
        assertThat(testResult(MissingMaxTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMaxTheory {
        @Property
        public void shouldHold(@InRange(min = "1976/12", format = "yyyy/MM") YearMonth d)
            throws Exception {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM");
            assertThat(d, greaterThanOrEqualTo(YearMonth.parse("1976/12", formatter)));
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
            @InRange(min = "1976/12", max = "1012/01", format = "yyyy/MM") YearMonth d) {
        }
    }
}

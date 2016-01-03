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

import java.time.Year;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.hasSingleFailureContaining;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

public class YearPropertyParameterTypesTest {
    @Test public void year() {
        assertThat(testResult(YearTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class YearTheory {
        @Property public void shouldHold(Year d) {
        }
    }

    @Test public void rangedYear() {
        assertThat(testResult(RangedYearTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedYearTheory {
        @Property public void shouldHold(
            @InRange(min = "1776", max = "1976", format = "yyyy") Year d) throws Exception {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");

            assertThat(
                d,
                allOf(
                    greaterThanOrEqualTo(Year.parse("1776", formatter)),
                    lessThanOrEqualTo(Year.parse("1976", formatter))));
        }
    }

    @Test public void malformedMin() {
        assertThat(
            testResult(MalformedMinYearTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMinYearTheory {
        @Property public void shouldHold(
            @InRange(min = "@#!@#@", max = "2563", format = "yyyy") Year d) {
        }
    }

    @Test public void malformedMax() {
        assertThat(
            testResult(MalformedMaxYearTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMaxYearTheory {
        @Property public void shouldHold(
            @InRange(min = "233", max = "*&@^#%$", format = "yyyy") Year d) {
        }
    }

    @Test public void malformedFormat() {
        assertThat(
            testResult(MalformedFormatYearTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedFormatYearTheory {
        @Property public void shouldHold(
            @InRange(min = "232", max = "4232", format = "*@&^#$") Year d) {
        }
    }

    @Test public void missingMin() {
        assertThat(testResult(MissingMinTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMinTheory {
        @Property public void shouldHold(@InRange(max = "2342", format = "yyyy") Year d)
            throws Exception {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
            assertThat(d, lessThanOrEqualTo(Year.parse("2342", formatter)));
        }
    }

    @Test public void missingMax() {
        assertThat(testResult(MissingMaxTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMaxTheory {
        @Property public void shouldHold(@InRange(min = "1223", format = "yyyy") Year d)
            throws Exception {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
            assertThat(d, greaterThanOrEqualTo(Year.parse("1223", formatter)));
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
            @InRange(min = "1231", max = "1201", format = "yyyy") Year d) {
        }
    }
}

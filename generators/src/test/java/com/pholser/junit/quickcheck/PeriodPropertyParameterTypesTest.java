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

import java.math.BigInteger;
import java.time.Period;
import java.time.format.DateTimeParseException;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.hasSingleFailureContaining;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

public class PeriodPropertyParameterTypesTest {
    private static final BigInteger TWELVE = BigInteger.valueOf(12);
    private static final BigInteger THIRTY_ONE = BigInteger.valueOf(31);

    @Test public void period() {
        assertThat(testResult(Periods.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Periods {
        @Property public void shouldHold(Period p) {
        }
    }

    @Test public void rangedPeriod() {
        assertThat(testResult(RangedPeriod.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedPeriod {
        @Property public void shouldHold(
            @InRange(min = "P1Y2M3D", max = "P36Y2M3D") Period p) {

            assertThat(
                toBigInteger(p),
                allOf(
                    greaterThanOrEqualTo(toBigInteger(Period.parse("P1Y2M3D"))),
                    lessThanOrEqualTo(toBigInteger(Period.parse("P36Y2M3D")))));
        }
    }

    @Test public void malformedMin() {
        assertThat(
            testResult(MalformedMinPeriod.class),
            hasSingleFailureContaining(DateTimeParseException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMinPeriod {
        @Property public void shouldHold(
            @InRange(min = "@#!@#@", max = "P36Y2M3D") Period p) {
        }
    }

    @Test public void malformedMax() {
        assertThat(
            testResult(MalformedMaxPeriod.class),
            hasSingleFailureContaining(DateTimeParseException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMaxPeriod {
        @Property public void shouldHold(
            @InRange(min = "P1Y2M3D", max = "*&@^#%$") Period p) {
        }
    }

    @Test public void missingMin() {
        assertThat(testResult(MissingMin.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMin {
        @Property public void shouldHold(@InRange(max = "P36Y2M3D") Period p) {
            assertThat(
                toBigInteger(p),
                lessThanOrEqualTo(toBigInteger(Period.parse("P36Y2M3D"))));
        }
    }

    @Test public void missingMax() {
        assertThat(testResult(MissingMax.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMax {
       @Property public void shouldHold(@InRange(min = "P36Y2M3D") Period p) {
            assertThat(
                toBigInteger(p),
                greaterThanOrEqualTo(toBigInteger(Period.parse("P36Y2M3D"))));
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
            @InRange(min = "P36Y2M3D", max = "P1Y2M3D") Period p) {
        }
    }

    private static BigInteger toBigInteger(Period period) {
        return BigInteger.valueOf(period.getYears())
            .multiply(TWELVE)
            .add(BigInteger.valueOf(period.getMonths()))
            .multiply(THIRTY_ONE)
            .add(BigInteger.valueOf(period.getDays()));
    }
}

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

import java.time.Instant;
import java.time.format.DateTimeParseException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.hasSingleFailureContaining;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

public class InstantPropertyParameterTypesTest {
    @Test public void instant() {
        assertThat(testResult(Instants.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Instants {
        @Property public void shouldHold(Instant i) {
        }
    }

    @Test public void rangedInstant() {
        assertThat(testResult(RangedInstant.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedInstant {
        @Property public void shouldHold(
            @InRange(
                min = "2012-01-01T00:00:00.0Z",
                max = "2012-12-31T23:59:59.999999999Z")
            Instant i) {

            assertThat(
                i,
                allOf(
                    greaterThanOrEqualTo(Instant.parse("2012-01-01T00:00:00.0Z")),
                    lessThanOrEqualTo(Instant.parse("2012-12-31T23:59:59.999999999Z"))));
        }
    }

    @Test public void malformedMin() {
        assertThat(
            testResult(MalformedMinInstant.class),
            hasSingleFailureContaining(DateTimeParseException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMinInstant {
        @Property public void shouldHold(
            @InRange(min = "@#!@#@", max = "2012-12-31T23:59:59.999999999Z") Instant i) {
        }
    }

    @Test public void malformedMax() {
        assertThat(
            testResult(MalformedMaxInstantTheory.class),
            hasSingleFailureContaining(DateTimeParseException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMaxInstantTheory {
        @Property public void shouldHold(
            @InRange(min = "06/01/2011T23:59:59.999999999Z", max = "*&@^#%$") Instant i) {
        }
    }

    @Test public void missingMin() {
        assertThat(testResult(MissingMin.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMin {
        @Property
        public void shouldHold(
            @InRange(max = "2012-12-31T23:59:59.999999999Z") Instant i) {

            assertThat(
                i,
                lessThanOrEqualTo(Instant.parse("2012-12-31T23:59:59.999999999Z")));
        }
    }

    @Test public void missingMax() {
        assertThat(testResult(MissingMax.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMax {
        @Property public void shouldHold(
            @InRange(min = "2012-12-31T23:59:59.999999999Z") Instant i) {

            assertThat(
                i,
                greaterThanOrEqualTo(Instant.parse("2012-12-31T23:59:59.999999999Z")));
        }
    }

    @Test public void backwardsRange() {
        assertThat(
            testResult(BackwardsRange.class),
            hasSingleFailureContaining(DateTimeParseException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class BackwardsRange {
        @Property public void shouldHold(
            @InRange(
                min = "2012-12-31T23:59:59.999999999Z",
                max = "12/01/2012T00:00:00.0Z")
            Instant i) {
        }
    }
}

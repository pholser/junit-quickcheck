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

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.hasSingleFailureContaining;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

public class InstantPropertyParameterTypesTest {
    @Test public void date() {
        assertThat(testResult(InstantTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class InstantTheory {
        @Property public void shouldHold(Instant d) {
        }
    }

    @Test public void rangedDate() {
        assertThat(testResult(RangedInstantTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedInstantTheory {
        @Property public void shouldHold(
            @InRange(min = "2012-01-01T00:00:00.0Z", max = "2012-12-31T23:59:59.999999999Z") Instant d)
            throws Exception {

            assertThat(
                d,
                allOf(
                    greaterThanOrEqualTo(Instant.parse("2012-01-01T00:00:00.0Z")),
                    lessThanOrEqualTo(Instant.parse("2012-12-31T23:59:59.999999999Z"))));
        }
    }

    @Test public void malformedMin() {
        assertThat(
            testResult(MalformedMinInstantTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMinInstantTheory {
        @Property public void shouldHold(
            @InRange(min = "@#!@#@", max = "2012-12-31T23:59:59.999999999Z") Instant d) {
        }
    }

    @Test public void malformedMax() {
        assertThat(
            testResult(MalformedMaxInstantTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMaxInstantTheory {
        @Property public void shouldHold(
            @InRange(min = "06/01/2011T23:59:59.999999999Z", max = "*&@^#%$") Instant d) {
        }
    }

    @Test public void missingMin() {
        assertThat(testResult(MissingMinTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMinTheory {
        @Property public void shouldHold(
            @InRange(max = "2012-12-31T23:59:59.999999999Z", format = "MM/dd/yyyy'T'HH:mm:ss.n") Instant d)
            throws Exception {

            assertThat(d, lessThanOrEqualTo(Instant.parse("2012-12-31T23:59:59.999999999Z")));
        }
    }

    @Test public void missingMax() {
        assertThat(testResult(MissingMaxTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMaxTheory {
        @Property public void shouldHold(@InRange(min = "2012-12-31T23:59:59.999999999Z") Instant d)
            throws Exception {

            assertThat(d, greaterThanOrEqualTo(Instant.parse("2012-12-31T23:59:59.999999999Z")));
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
            @InRange(min = "2012-12-31T23:59:59.999999999Z", max = "12/01/2012T00:00:00.0Z") Instant d) {
        }
    }
}

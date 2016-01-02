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

import java.time.Duration;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.hasSingleFailureContaining;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

public class DurationPropertyParameterTypesTest {
    @Test public void duration() {
        assertThat(testResult(DurationTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class DurationTheory {
        @Property public void shouldHold(Duration d) {
        }
    }

    @Test public void rangedDuration() {
        assertThat(testResult(RangedDurationTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedDurationTheory {
        @Property public void shouldHold(
            @InRange(min = "PT-2562047788015215H-30M-8S", max = "PT2562047788015215H30M7.999999999S")
            Duration d)
            throws Exception {

            assertThat(
                d,
                allOf(
                    greaterThanOrEqualTo(Duration.parse("PT-2562047788015215H-30M-8S")),
                    lessThanOrEqualTo(Duration.parse("PT2562047788015215H30M7.999999999S"))));
        }
    }

    @Test public void malformedMin() {
        assertThat(
            testResult(MalformedMinDurationTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMinDurationTheory {
        @Property public void shouldHold(
            @InRange(min = "@#!@#@", max = "PT2562047788015215H30M7.999999999S") Duration d) {
        }
    }

    @Test public void malformedMax() {
        assertThat(
            testResult(MalformedMaxDurationTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MalformedMaxDurationTheory {
        @Property public void shouldHold(
            @InRange(min = "PT-2562047788015215H-30M-8S", max = "*&@^#%$") Duration d) {
        }
    }

    @Test public void missingMin() {
        assertThat(testResult(MissingMinTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMinTheory {
        @Property public void shouldHold(@InRange(max = "PT2562047788015215H30M7.999999999S") Duration d)
            throws Exception {

            assertThat(d, lessThanOrEqualTo(Duration.parse("PT2562047788015215H30M7.999999999S")));
        }
    }

    @Test public void missingMax() {
        assertThat(testResult(MissingMaxTheory.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MissingMaxTheory {
        @Property public void shouldHold(@InRange(min = "PT-2562047788015215H-30M-8S") Duration d)
            throws Exception {

            assertThat(d, greaterThanOrEqualTo(Duration.parse("PT-2562047788015215H-30M-8S")));
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
            @InRange(min = "PT2562047788015215H30M7.999999999S", max = "PT-2562047788015215H-30M-8S")
            Duration d) {
        }
    }
}

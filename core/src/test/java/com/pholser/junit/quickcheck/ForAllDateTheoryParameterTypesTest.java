/*
 The MIT License

 Copyright (c) 2010-2013 Paul R. Holser, Jr.

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

import java.text.SimpleDateFormat;
import java.util.Date;

import com.pholser.junit.quickcheck.generator.InRange;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ForAllDateTheoryParameterTypesTest {
    @Test public void date() {
        assertThat(testResult(DateTheory.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class DateTheory {
        @Theory public void shouldHold(@ForAll Date d) {
        }
    }

    @Test public void rangedDate() {
        assertThat(testResult(RangedDateTheory.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedDateTheory {
        @Theory public void shouldHold(@ForAll
                                       @InRange(min = "01/01/2012", max = "12/31/2012", format = "MM/dd/yyyy") Date d)
            throws Exception {

            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            assertThat(d,
                allOf(greaterThanOrEqualTo(formatter.parse("01/01/2012")),
                    lessThanOrEqualTo(formatter.parse("12/31/2012"))));
        }
    }

    @Test public void malformedMin() {
        assertThat(testResult(MalformedMinDateTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(Theories.class)
    public static class MalformedMinDateTheory {
        @Theory public void shouldHold(@ForAll
                                       @InRange(min = "@#!@#@", max = "12/31/2012", format = "MM/dd/yyyy") Date d) {
        }
    }

    @Test public void malformedMax() {
        assertThat(testResult(MalformedMaxDateTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(Theories.class)
    public static class MalformedMaxDateTheory {
        @Theory public void shouldHold(@ForAll
                                       @InRange(min = "06/01/2011", max = "*&@^#%$", format = "MM/dd/yyyy") Date d) {
        }
    }

    @Test public void malformedFormat() {
        assertThat(testResult(MalformedFormatDateTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(Theories.class)
    public static class MalformedFormatDateTheory {
        @Theory public void shouldHold(@ForAll
                                       @InRange(min = "06/01/2011", max = "06/30/2011", format = "*@&^#$") Date d) {
        }
    }

    @Test public void missingMin() {
        assertThat(testResult(MissingMinTheory.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class MissingMinTheory {
        @Theory public void shouldHold(@ForAll @InRange(max = "12/31/2012", format = "MM/dd/yyyy") Date d)
            throws Exception {

            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            assertThat(d, lessThanOrEqualTo(formatter.parse("12/31/2012")));
        }
    }

    @Test public void missingMax() {
        assertThat(testResult(MissingMaxTheory.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class MissingMaxTheory {
        @Theory public void shouldHold(@ForAll @InRange(min = "12/31/2012", format = "MM/dd/yyyy") Date d)
            throws Exception {

            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            assertThat(d, greaterThanOrEqualTo(formatter.parse("12/31/2012")));
        }
    }

    @Test public void backwardsRange() {
        assertThat(testResult(BackwardsRangeTheory.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(Theories.class)
    public static class BackwardsRangeTheory {
        @Theory public void shouldHold(@ForAll
                                       @InRange(min = "12/31/2012", max = "12/01/2012", format = "MM/dd/yyyy") Date d) {
        }
    }
}

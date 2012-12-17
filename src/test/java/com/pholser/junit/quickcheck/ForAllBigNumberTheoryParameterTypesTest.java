/*
 The MIT License

 Copyright (c) 2010-2012 Paul R. Holser, Jr.

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

import java.math.BigDecimal;
import java.math.BigInteger;

import com.pholser.junit.quickcheck.generator.InRange;
import org.junit.Test;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ForAllBigNumberTheoryParameterTypesTest {
    @Test
    public void bigInteger() {
        assertThat(testResult(BigIntegerTheory.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class BigIntegerTheory {
        @Theory
        public void shouldHold(@ForAll BigInteger i) {
        }
    }

    @Test
    public void rangedBigIntegerWithBackwardsRange() {
        assertThat(testResult(RangedBigIntegerWithBackwardsRange.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(Theories.class)
    public static class RangedBigIntegerWithBackwardsRange {
        @Theory
        public void shouldHold(@ForAll @InRange(min = "1", max = "0") BigInteger i) {
        }
    }

    @Test
    public void rangedBigIntegerWithMalformedMin() {
        assertThat(testResult(RangedBigIntegerWithMalformedMin.class),
            hasSingleFailureContaining(NumberFormatException.class.getName()));
    }

    @RunWith(Theories.class)
    public static class RangedBigIntegerWithMalformedMin {
        @Theory
        public void shouldHold(@ForAll @InRange(min = "-(*&!(#*!@", max = "0") BigInteger i) {
        }
    }

    @Test
    public void rangedBigIntegerWithMalformedMax() {
        assertThat(testResult(RangedBigIntegerWithMalformedMax.class),
            hasSingleFailureContaining(NumberFormatException.class.getName()));
    }

    @RunWith(Theories.class)
    public static class RangedBigIntegerWithMalformedMax {
        @Theory
        public void shouldHold(@ForAll @InRange(min = "-1234", max = "237KJAHDLKAJHS") BigInteger i) {
        }
    }

    @Test
    public void rangedBigIntegerWithNoMin() {
        assertThat(testResult(RangedBigIntegerWithNoMin.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedBigIntegerWithNoMin {
        @Theory
        public void shouldHold(@ForAll @InRange(max = "0") BigInteger i) {
            assertThat(i, lessThanOrEqualTo(BigInteger.ZERO));
        }
    }

    @Test
    public void rangedBigIntegerWithNoMax() {
        assertThat(testResult(RangedBigIntegerWithNoMax.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedBigIntegerWithNoMax {
        @Theory
        public void shouldHold(@ForAll @InRange(min = "1") BigInteger i) {
            assertThat(i, greaterThanOrEqualTo(BigInteger.ONE));
        }
    }

    @Test
    public void rangedBigIntegerWithMinAndMax() {
        assertThat(testResult(RangedBigIntegerWithMinAndMax.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedBigIntegerWithMinAndMax {
        @Theory
        public void shouldHold(@ForAll @InRange(min = "1", max = "10000000") BigInteger i) {
            assertThat(i, allOf(greaterThanOrEqualTo(BigInteger.ONE), lessThanOrEqualTo(new BigInteger("10000000"))));
        }
    }

    @Test
    public void bigDecimal() {
        assertThat(testResult(BigDecimalTheory.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class BigDecimalTheory {
        @Theory
        public void shouldHold(@ForAll BigDecimal d) {
        }
    }
}

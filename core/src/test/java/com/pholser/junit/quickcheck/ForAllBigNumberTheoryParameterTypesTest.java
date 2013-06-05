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

import java.math.BigDecimal;
import java.math.BigInteger;

import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Precision;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ForAllBigNumberTheoryParameterTypesTest {
    @Test public void bigInteger() {
        assertThat(testResult(BigIntegerTheory.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class BigIntegerTheory {
        @Theory public void shouldHold(@ForAll BigInteger i) {
        }
    }

    @Test public void rangedBigIntegerWithBackwardsRange() {
        assertThat(testResult(RangedBigIntegerWithBackwardsRange.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(Theories.class)
    public static class RangedBigIntegerWithBackwardsRange {
        @Theory public void shouldHold(@ForAll @InRange(min = "1", max = "0") BigInteger i) {
        }
    }

    @Test public void rangedBigIntegerWithMalformedMin() {
        assertThat(testResult(RangedBigIntegerWithMalformedMin.class),
            hasSingleFailureContaining(NumberFormatException.class.getName()));
    }

    @RunWith(Theories.class)
    public static class RangedBigIntegerWithMalformedMin {
        @Theory public void shouldHold(@ForAll @InRange(min = "-(*&!(#*!@", max = "0") BigInteger i) {
        }
    }

    @Test public void rangedBigIntegerWithMalformedMax() {
        assertThat(testResult(RangedBigIntegerWithMalformedMax.class),
            hasSingleFailureContaining(NumberFormatException.class.getName()));
    }

    @RunWith(Theories.class)
    public static class RangedBigIntegerWithMalformedMax {
        @Theory public void shouldHold(@ForAll @InRange(min = "-1234", max = "237KJAHDLKAJHS") BigInteger i) {
        }
    }

    @Test public void rangedBigIntegerWithNoMin() {
        assertThat(testResult(RangedBigIntegerWithNoMin.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedBigIntegerWithNoMin {
        @Theory public void shouldHold(@ForAll @InRange(max = "0") BigInteger i) {
            assertThat(i, lessThanOrEqualTo(BigInteger.ZERO));
        }
    }

    @Test public void rangedBigIntegerWithNoMax() {
        assertThat(testResult(RangedBigIntegerWithNoMax.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedBigIntegerWithNoMax {
        @Theory public void shouldHold(@ForAll @InRange(min = "1") BigInteger i) {
            assertThat(i, greaterThanOrEqualTo(BigInteger.ONE));
        }
    }

    @Test public void rangedBigIntegerWithMinAndMax() {
        assertThat(testResult(RangedBigIntegerWithMinAndMax.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedBigIntegerWithMinAndMax {
        @Theory public void shouldHold(@ForAll @InRange(min = "1", max = "10000000") BigInteger i) {
            assertThat(i, allOf(greaterThanOrEqualTo(BigInteger.ONE), lessThanOrEqualTo(new BigInteger("10000000"))));
        }
    }

    @Test public void bigDecimal() {
        assertThat(testResult(BigDecimalTheory.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class BigDecimalTheory {
        @Theory public void shouldHold(@ForAll BigDecimal d) {
        }
    }

    @Test public void bigDecimalWithSpecifiedPrecision() {
        assertThat(testResult(BigDecimalTheory.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class BigDecimalWithSpecifiedPrecisionTheory {
        @Theory public void shouldHold(@ForAll @Precision(scale = 5) BigDecimal d) {
            assertEquals(5, d.scale());
        }
    }

    @Test public void rangedBigDecimalWithBackwardsRange() {
        assertThat(testResult(RangedBigDecimalWithBackwardsRange.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(Theories.class)
    public static class RangedBigDecimalWithBackwardsRange {
        @Theory public void shouldHold(@ForAll @InRange(min = "1.02", max = "1.01999") BigDecimal d) {
        }
    }

    @Test public void rangedBigDecimalWithMalformedMin() {
        assertThat(testResult(RangedBigDecimalWithMalformedMin.class),
            hasSingleFailureContaining(NumberFormatException.class.getName()));
    }

    @RunWith(Theories.class)
    public static class RangedBigDecimalWithMalformedMin {
        @Theory public void shouldHold(@ForAll @InRange(min = "ZXCZXC", max = "-34.56") BigDecimal d) {
        }
    }

    @Test public void rangedBigDecimalWithMalformedMax() {
        assertThat(testResult(RangedBigDecimalWithMalformedMax.class),
            hasSingleFailureContaining(NumberFormatException.class.getName()));
    }

    @RunWith(Theories.class)
    public static class RangedBigDecimalWithMalformedMax {
        @Theory public void shouldHold(@ForAll @InRange(min = "-1234.5678", max = "***@*@*") BigInteger i) {
        }
    }

    @Test public void rangedBigDecimalWithNoMin() {
        assertThat(testResult(RangedBigDecimalWithNoMin.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedBigDecimalWithNoMin {
        @Theory public void shouldHold(@ForAll @InRange(max = "-0.000123123") BigDecimal d) {
            assertEquals(9, d.scale());
            assertThat(d, lessThanOrEqualTo(new BigDecimal("-0.000123123")));
        }
    }

    @Test public void rangedBigDecimalWithNoMinLesserSpecifiedPrecision() {
        assertThat(testResult(RangedBigDecimalWithNoMinLesserSpecifiedPrecision.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedBigDecimalWithNoMinLesserSpecifiedPrecision {
        @Theory public void shouldHold(@ForAll @InRange(max = "-0.000123123") @Precision(scale = 8) BigDecimal d) {
            assertEquals(9, d.scale());
            assertThat(d, lessThanOrEqualTo(new BigDecimal("-0.000123123")));
        }
    }

    @Test public void rangedBigDecimalWithNoMinGreaterSpecifiedPrecision() {
        assertThat(testResult(RangedBigDecimalWithNoMinGreaterSpecifiedPrecision.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedBigDecimalWithNoMinGreaterSpecifiedPrecision {
        @Theory public void shouldHold(@ForAll @InRange(max = "-0.000123123") @Precision(scale = 10) BigDecimal d) {
            assertEquals(10, d.scale());
            assertThat(d, lessThanOrEqualTo(new BigDecimal("-0.000123123")));
        }
    }

    @Test public void rangedBigDecimalWithNoMax() {
        assertThat(testResult(RangedBigDecimalWithNoMax.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedBigDecimalWithNoMax {
        @Theory public void shouldHold(@ForAll @InRange(min = "1.23456789") BigDecimal d) {
            assertEquals(8, d.scale());
            assertThat(d, greaterThanOrEqualTo(new BigDecimal("1.23456789")));
        }
    }

    @Test public void rangedBigDecimalWithNoMaxLesserSpecifiedPrecision() {
        assertThat(testResult(RangedBigDecimalWithNoMaxLesserSpecifiedPrecision.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedBigDecimalWithNoMaxLesserSpecifiedPrecision {
        @Theory public void shouldHold(@ForAll @InRange(min = "1.23456789") @Precision(scale = 7) BigDecimal d) {
            assertEquals(8, d.scale());
            assertThat(d, greaterThanOrEqualTo(new BigDecimal("1.23456789")));
        }
    }

    @Test public void rangedBigDecimalWithNoMaxGreaterSpecifiedPrecision() {
        assertThat(testResult(RangedBigDecimalWithNoMaxGreaterSpecifiedPrecision.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedBigDecimalWithNoMaxGreaterSpecifiedPrecision {
        @Theory public void shouldHold(@ForAll @InRange(min = "1.23456789") @Precision(scale = 9) BigDecimal d) {
            assertEquals(9, d.scale());
            assertThat(d, greaterThanOrEqualTo(new BigDecimal("1.23456789")));
        }
    }

    @Test public void rangedBigDecimalWithMinAndMax() {
        assertThat(testResult(RangedBigDecimalWithMinAndMax.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedBigDecimalWithMinAndMax {
        @Theory public void shouldHold(@ForAll
                                       @InRange(min = "-123456789.0123", max = "99999999.87654321") BigDecimal d) {
            assertEquals(8, d.scale());
            assertThat(d, allOf(
                greaterThanOrEqualTo(new BigDecimal("-123456789.0123")),
                lessThanOrEqualTo(new BigDecimal("99999999.87654321"))));
        }
    }

    @Test public void rangedBigDecimalWithMinAndMaxLesserSpecifiedPrecision() {
        assertThat(testResult(RangedBigDecimalWithMinAndMaxLesserSpecifiedPrecision.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedBigDecimalWithMinAndMaxLesserSpecifiedPrecision {
        @Theory public void shouldHold(@ForAll
                                       @InRange(min = "-123456789.0123", max = "99999999.87654321")
                                       @Precision(scale = 7) BigDecimal d) {
            assertEquals(8, d.scale());
            assertThat(d, allOf(
                greaterThanOrEqualTo(new BigDecimal("-123456789.0123")),
                lessThanOrEqualTo(new BigDecimal("99999999.87654321"))));
        }
    }

    @Test public void rangedBigDecimalWithMinAndMaxGreaterSpecifiedPrecision() {
        assertThat(testResult(RangedBigDecimalWithMinAndMaxGreaterSpecifiedPrecision.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedBigDecimalWithMinAndMaxGreaterSpecifiedPrecision {
        @Theory public void shouldHold(@ForAll @InRange(min = "-123456789.0123", max = "99999999.87654321")
                                       @Precision(scale = 11) BigDecimal d) {
            assertEquals(11, d.scale());
            assertThat(d, allOf(
                greaterThanOrEqualTo(new BigDecimal("-123456789.0123")),
                lessThanOrEqualTo(new BigDecimal("99999999.87654321"))));
        }
    }
}

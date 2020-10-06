/*
 The MIT License

 Copyright (c) 2010-2020 Paul R. Holser, Jr.

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

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.failureCountIs;
import static org.junit.experimental.results.ResultMatchers.hasSingleFailureContaining;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

import com.google.common.collect.Iterables;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Precision;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

public class BigNumberPropertyParameterTypesTest {
    @Test public void bigInteger() {
        assertThat(testResult(BigIntegerProperty.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class BigIntegerProperty {
        @Property public void shouldHold(BigInteger i) {
        }
    }

    @Test public void rangedBigIntegerWithBackwardsRange() {
        assertThat(
            testResult(RangedBigIntegerWithBackwardsRange.class),
            hasSingleFailureContaining(
                IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedBigIntegerWithBackwardsRange {
        @Property public void shouldHold(
            @InRange(min = "1", max = "0") BigInteger i) {
        }
    }

    @Test public void rangedBigIntegerWithMalformedMin() {
        assertThat(
            testResult(RangedBigIntegerWithMalformedMin.class),
            hasSingleFailureContaining(NumberFormatException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedBigIntegerWithMalformedMin {
        @Property public void shouldHold(
            @InRange(min = "-(*&!(#*!@", max = "0") BigInteger i) {
        }
    }

    @Test public void rangedBigIntegerWithMalformedMax() {
        assertThat(
            testResult(RangedBigIntegerWithMalformedMax.class),
            hasSingleFailureContaining(NumberFormatException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedBigIntegerWithMalformedMax {
        @Property public void shouldHold(
            @InRange(min = "-1234", max = "237KJAHDLKAJHS") BigInteger i) {
        }
    }

    @Test public void rangedBigIntegerWithNoMin() {
        assertThat(
            testResult(RangedBigIntegerWithNoMin.class),
            isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedBigIntegerWithNoMin {
        @Property public void shouldHold(@InRange(max = "0") BigInteger i) {
            assertThat(i, lessThanOrEqualTo(BigInteger.ZERO));
        }
    }

    @Test public void rangedBigIntegerWithNoMax() {
        assertThat(
            testResult(RangedBigIntegerWithNoMax.class),
            isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedBigIntegerWithNoMax {
        @Property public void shouldHold(@InRange(min = "1") BigInteger i) {
            assertThat(i, greaterThanOrEqualTo(BigInteger.ONE));
        }
    }

    @Test public void rangedBigIntegerWithMinAndMax() {
        assertThat(
            testResult(RangedBigIntegerWithMinAndMax.class),
            isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedBigIntegerWithMinAndMax {
        @Property public void shouldHold(
            @InRange(min = "1", max = "10000000") BigInteger i) {

            assertThat(
                i,
                allOf(
                    greaterThanOrEqualTo(BigInteger.ONE),
                    lessThanOrEqualTo(new BigInteger("10000000"))));
        }
    }

    @Test public void shrinkingPositiveBigInteger() {
        assertThat(
            testResult(ShrinkingPositiveBigInteger.class),
            failureCountIs(1));
        assertEquals(
            BigInteger.valueOf(12341234),
            Iterables.getLast(ShrinkingPositiveBigInteger.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPositiveBigInteger {
        static final List<BigInteger> values = new ArrayList<>();

        @Property public void shouldHold(
            @InRange(min = "12341234", max = "555555555555") BigInteger i) {

            values.add(i);

            fail();
        }
    }

    @Test public void shrinkingNegativeBigInteger() {
        assertThat(
            testResult(ShrinkingNegativeBigInteger.class),
            failureCountIs(1));

        assertEquals(
            BigInteger.valueOf(-114477),
            Iterables.getLast(ShrinkingNegativeBigInteger.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingNegativeBigInteger {
        static final List<BigInteger> values = new ArrayList<>();

        @Property public void shouldHold(
            @InRange(min = "-999888777666", max = "-114477") BigInteger i) {

            values.add(i);

            fail();
        }
    }

    @Test public void shrinkingBigIntegerStraddlingZero() {
        assertThat(
            testResult(ShrinkingBigIntegerStraddlingZero.class),
            failureCountIs(1));

        assertThat(
            Iterables.getLast(ShrinkingBigIntegerStraddlingZero.values),
            lessThanOrEqualTo(BigInteger.TEN));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingBigIntegerStraddlingZero {
        static final List<BigInteger> values = new ArrayList<>();

        @Property public void shouldHold(BigInteger i) {
            values.add(i);

            assertThat(i.abs(), lessThan(BigInteger.TEN));
        }
    }

    @Test public void bigDecimal() {
        assertThat(testResult(BigDecimalProperty.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class BigDecimalProperty {
        @Property public void shouldHold(BigDecimal d) {
        }
    }

    @Test public void bigDecimalWithSpecifiedPrecision() {
        assertThat(
            testResult(BigDecimalWithSpecifiedPrecision.class),
            isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class BigDecimalWithSpecifiedPrecision {
        @Property public void shouldHold(@Precision(scale = 5) BigDecimal d) {
            assertEquals(5, d.scale());
        }
    }

    @Test public void rangedBigDecimalWithBackwardsRange() {
        assertThat(
            testResult(RangedBigDecimalWithBackwardsRange.class),
            hasSingleFailureContaining(
                IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedBigDecimalWithBackwardsRange {
        @Property public void shouldHold(
            @InRange(min = "1.02", max = "1.01999") BigDecimal d) {
        }
    }

    @Test public void rangedBigDecimalWithMalformedMin() {
        assertThat(
            testResult(RangedBigDecimalWithMalformedMin.class),
            hasSingleFailureContaining(NumberFormatException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedBigDecimalWithMalformedMin {
        @Property public void shouldHold(
            @InRange(min = "ZXCZXC", max = "-34.56") BigDecimal d) {
        }
    }

    @Test public void rangedBigDecimalWithMalformedMax() {
        assertThat(
            testResult(RangedBigDecimalWithMalformedMax.class),
            hasSingleFailureContaining(NumberFormatException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedBigDecimalWithMalformedMax {
        @Property public void shouldHold(
            @InRange(min = "-1234.5678", max = "***@*@*") BigInteger i) {
        }
    }

    @Test public void rangedBigDecimalWithNoMin() {
        assertThat(
            testResult(RangedBigDecimalWithNoMin.class),
            isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedBigDecimalWithNoMin {
        @Property public void shouldHold(
            @InRange(max = "-0.000123123") BigDecimal d) {

            assertEquals(9, d.scale());
            assertThat(d, lessThanOrEqualTo(new BigDecimal("-0.000123123")));
        }
    }

    @Test public void rangedBigDecimalWithNoMinLesserSpecifiedPrecision() {
        assertThat(
            testResult(RangedBigDecimalWithNoMinLesserSpecifiedPrecision.class),
            isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedBigDecimalWithNoMinLesserSpecifiedPrecision {
        @Property public void shouldHold(
            @InRange(max = "-0.000123123") @Precision(scale = 8) BigDecimal d) {

            assertEquals(9, d.scale());
            assertThat(d, lessThanOrEqualTo(new BigDecimal("-0.000123123")));
        }
    }

    @Test public void rangedBigDecimalWithNoMinGreaterSpecifiedPrecision() {
        assertThat(
            testResult(RangedBigDecimalWithNoMinGreaterSpecifiedPrecision.class),
            isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedBigDecimalWithNoMinGreaterSpecifiedPrecision {
        @Property public void shouldHold(
            @InRange(max = "-0.000123123") @Precision(scale = 10)
                BigDecimal d) {

            assertEquals(10, d.scale());
            assertThat(d, lessThanOrEqualTo(new BigDecimal("-0.000123123")));
        }
    }

    @Test public void rangedBigDecimalWithNoMax() {
        assertThat(
            testResult(RangedBigDecimalWithNoMax.class),
            isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedBigDecimalWithNoMax {
        @Property public void shouldHold(
            @InRange(min = "1.23456789") BigDecimal d) {

            assertEquals(8, d.scale());
            assertThat(d, greaterThanOrEqualTo(new BigDecimal("1.23456789")));
        }
    }

    @Test public void rangedBigDecimalWithNoMaxLesserSpecifiedPrecision() {
        assertThat(
            testResult(RangedBigDecimalWithNoMaxLesserSpecifiedPrecision.class),
            isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedBigDecimalWithNoMaxLesserSpecifiedPrecision {
        @Property public void shouldHold(
            @InRange(min = "1.23456789") @Precision(scale = 7) BigDecimal d) {

            assertEquals(8, d.scale());
            assertThat(d, greaterThanOrEqualTo(new BigDecimal("1.23456789")));
        }
    }

    @Test public void rangedBigDecimalWithNoMaxGreaterSpecifiedPrecision() {
        assertThat(
            testResult(RangedBigDecimalWithNoMaxGreaterSpecifiedPrecision.class),
            isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedBigDecimalWithNoMaxGreaterSpecifiedPrecision {
        @Property public void shouldHold(
            @InRange(min = "1.23456789") @Precision(scale = 9) BigDecimal d) {

            assertEquals(9, d.scale());
            assertThat(d, greaterThanOrEqualTo(new BigDecimal("1.23456789")));
        }
    }

    @Test public void rangedBigDecimalWithMinAndMax() {
        assertThat(
            testResult(RangedBigDecimalWithMinAndMax.class),
            isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedBigDecimalWithMinAndMax {
        @Property public void shouldHold(
            @InRange(min = "-123456789.0123", max = "99999999.87654321")
                BigDecimal d) {

            assertEquals(8, d.scale());
            assertThat(
                d,
                allOf(
                    greaterThanOrEqualTo(new BigDecimal("-123456789.0123")),
                    lessThanOrEqualTo(new BigDecimal("99999999.87654321"))));
        }
    }

    @Test public void rangedBigDecimalWithMinAndMaxLesserSpecifiedPrecision() {
        assertThat(
            testResult(RangedBigDecimalWithMinAndMaxLesserSpecifiedPrecision.class),
            isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedBigDecimalWithMinAndMaxLesserSpecifiedPrecision {
        @Property public void shouldHold(
            @InRange(min = "-123456789.0123", max = "99999999.87654321")
            @Precision(scale = 7)
                BigDecimal d) {

            assertEquals(8, d.scale());
            assertThat(
                d,
                allOf(
                    greaterThanOrEqualTo(new BigDecimal("-123456789.0123")),
                    lessThanOrEqualTo(new BigDecimal("99999999.87654321"))));
        }
    }

    @Test public void
    rangedBigDecimalWithMinAndMaxGreaterSpecifiedPrecision() {
        assertThat(
            testResult(RangedBigDecimalWithMinAndMaxGreaterSpecifiedPrecision.class),
            isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class
    RangedBigDecimalWithMinAndMaxGreaterSpecifiedPrecision {
        @Property public void shouldHold(
            @InRange(min = "-123456789.0123", max = "99999999.87654321")
            @Precision(scale = 11)
                BigDecimal d) {

            assertEquals(11, d.scale());
            assertThat(
                d,
                allOf(
                    greaterThanOrEqualTo(new BigDecimal("-123456789.0123")),
                    lessThanOrEqualTo(new BigDecimal("99999999.87654321"))));
        }
    }

    @Test public void bigDecimalByAggregateAnnotations() {
        assertThat(
            testResult(BigDecimalByAggregateAnnotations.class),
            isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class BigDecimalByAggregateAnnotations {
        @Target({PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE})
        @Retention(RUNTIME)
        @InRange(min = "0", max = "20")
        @Precision(scale = 2)
        public @interface SmallChange {
        }

        @Property public void shouldHold(@SmallChange BigDecimal d) {
            assertEquals(2, d.scale());
            assertThat(
                d,
                allOf(
                    greaterThanOrEqualTo(BigDecimal.ZERO),
                    lessThanOrEqualTo(new BigDecimal("20"))));
        }
    }

    @Test public void shrinkingPositiveBigDecimal() {
        assertThat(
            testResult(ShrinkingPositiveBigDecimal.class),
            failureCountIs(1));
        assertEquals(
            new BigDecimal("123.45678909891"),
            Iterables.getLast(ShrinkingPositiveBigDecimal.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPositiveBigDecimal {
        static final List<BigDecimal> values = new ArrayList<>();

        @Property public void shouldHold(
            @InRange(min = "123.45678909891", max = "5555.23213123123")
                BigDecimal d) {

            values.add(d);

            fail();
        }
    }

    @Test public void shrinkingNegativeBigDecimal() {
        assertThat(
            testResult(ShrinkingNegativeBigDecimal.class),
            failureCountIs(1));

        assertThat(
            Iterables.getLast(ShrinkingNegativeBigDecimal.values),
            lessThanOrEqualTo(new BigDecimal("-114477.1234123412341234")));
        assertThat(
            Iterables.getLast(ShrinkingNegativeBigDecimal.values),
            greaterThanOrEqualTo(new BigDecimal("-999888777666.55443322211")));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingNegativeBigDecimal {
        static List<BigDecimal> values = new ArrayList<>();

        @Property public void shouldHold(
            @InRange(
                min = "-999888777666.55443322211",
                max = "-114477.1234123412341234")
                BigDecimal d) {

            values.add(d);

            fail();
        }
    }

    @Test public void shrinkingNegativeBigDecimalTowardsZero() {
        assertThat(
            testResult(ShrinkingNegativeBigDecimalTowardsZero.class),
            failureCountIs(1));

        assertThat(
            Iterables.getLast(ShrinkingNegativeBigDecimalTowardsZero.values),
            equalTo(BigDecimal.ZERO));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingNegativeBigDecimalTowardsZero {
        static final List<BigDecimal> values = new ArrayList<>();

        @Property public void shouldHold(
            @InRange(min = "-999999999.999", max = "0") BigDecimal d) {

            values.add(d);

            fail();
        }
    }

    @Test public void shrinkingNegativeBigIntegerTowardsZero() {
        assertThat(
            testResult(ShrinkingNegativeBigIntegerTowardsZero.class),
            failureCountIs(1));
        assertThat(
            Iterables.getLast(ShrinkingNegativeBigIntegerTowardsZero.values),
            equalTo(BigInteger.ZERO));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingNegativeBigIntegerTowardsZero {
        static final List<BigInteger> values = new ArrayList<>();

        @Property public void shouldHold(
            @InRange(min = "-999999999", max = "0") BigInteger d) {

            values.add(d);

            fail();
        }
    }

    @Test public void shrinkingPositiveBigDecimalTowardsZero() {
        assertThat(
            testResult(ShrinkingPositiveBigDecimalTowardsZero.class),
            failureCountIs(1));

        assertThat(
            Iterables.getLast(ShrinkingPositiveBigDecimalTowardsZero.values),
            equalTo(BigDecimal.ZERO));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPositiveBigDecimalTowardsZero {
        static final List<BigDecimal> values = new ArrayList<>();

        @Property public void shouldHold(
            @InRange(min = "0", max = "999999999.999") BigDecimal d) {

            values.add(d);

            fail();
        }
    }

    @Test public void shrinkingBigDecimalStraddlingZero() {
        assertThat(
            testResult(ShrinkingBigDecimalStraddlingZero.class),
            failureCountIs(1));
        assertThat(
            Iterables.getLast(ShrinkingBigDecimalStraddlingZero.values),
            lessThanOrEqualTo(BigDecimal.TEN));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingBigDecimalStraddlingZero {
        static final List<BigDecimal> values = new ArrayList<>();

        @Property public void shouldHold(BigDecimal d) {
            values.add(d);

            assertThat(d.abs(), lessThan(BigDecimal.TEN));
        }
    }
}

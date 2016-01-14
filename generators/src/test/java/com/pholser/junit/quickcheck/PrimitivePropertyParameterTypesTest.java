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

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Iterables;
import com.pholser.junit.quickcheck.generator.GeneratorConfigurationException;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.RangeAttributes;
import com.pholser.junit.quickcheck.generator.ValuesOf;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Annotations.*;
import static com.pholser.junit.quickcheck.Lists.*;
import static java.util.Arrays.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class PrimitivePropertyParameterTypesTest {
    @Test public void primitiveBoolean() {
        assertThat(testResult(PrimitiveBoolean.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveBoolean {
        @Property public void shouldHold(boolean b) {
        }
    }

    @Test public void wrapperBoolean() {
        assertThat(testResult(WrapperBoolean.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperBoolean {
        @Property public void shouldHold(Boolean b) {
        }
    }

    @Test public void primitiveBooleanWithValuesOf() {
        assertThat(testResult(PrimitiveBooleanWithValuesOf.class), isSuccessful());
        assertEquals(repeat(asList(false, true), 50), PrimitiveBooleanWithValuesOf.values);
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveBooleanWithValuesOf {
        static List<Boolean> values = new ArrayList<>();

        @Property public void shouldHold(@ValuesOf boolean b) {
            values.add(b);
        }
    }

    @Test public void wrapperBooleanWithValuesOf() {
        assertThat(testResult(WrapperBooleanWithValuesOf.class), isSuccessful());
        assertEquals(repeat(asList(false, true), 50), WrapperBooleanWithValuesOf.values);
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperBooleanWithValuesOf {
        static List<Boolean> values = new ArrayList<>();

        @Property public void shouldHold(@ValuesOf Boolean b) {
            values.add(b);
        }
    }

    @Test public void shrinkingBooleanFromTrue() {
        assertThat(testResult(ShrinkingBooleanFromTrue.class), failureCountIs(1));

        int trueIndex = ShrinkingBooleanFromTrue.values.lastIndexOf(true);
        assumeThat(trueIndex, not(equalTo(-1)));
        assertFalse(ShrinkingBooleanFromTrue.values.get(trueIndex + 1));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingBooleanFromTrue {
        static List<Boolean> values = new ArrayList<>();

        @Property public void shouldHold(boolean b) {
            values.add(b);

            assumeTrue(b);
            fail();
        }
    }

    @Test public void shrinkingBooleanFromFalse() {
        assertThat(testResult(ShrinkingBooleanFromFalse.class), failureCountIs(1));

        int falseIndex = ShrinkingBooleanFromFalse.values.lastIndexOf(false);
        assertThat(falseIndex, equalTo(ShrinkingBooleanFromFalse.values.size() - 1));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingBooleanFromFalse {
        static List<Boolean> values = new ArrayList<>();

        @Property public void shouldHold(boolean b) {
            values.add(b);

            assumeFalse(b);
            fail();
        }
    }

    @Test public void primitiveByte() {
        assertThat(testResult(PrimitiveByte.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveByte {
        @Property public void shouldHold(byte b) {
        }
    }

    @Test public void rangedPrimitiveByte() {
        assertThat(testResult(RangedPrimitiveByte.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedPrimitiveByte {
        @Property public void shouldHold(@InRange(min = "-23", max = "34") byte b) {
            assertThat(b, allOf(greaterThanOrEqualTo((byte) -23), lessThanOrEqualTo((byte) 34)));
        }
    }

    @Test public void leftOpenEndedRangedPrimitiveByte() {
        assertThat(testResult(LeftOpenEndedRangedPrimitiveByte.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class LeftOpenEndedRangedPrimitiveByte {
        @Property public void shouldHold(@InRange(max = "0") byte b) {
            assertThat(b, lessThanOrEqualTo((byte) 0));
        }
    }

    @Test public void rightOpenEndedRangedPrimitiveByte() {
        assertThat(testResult(RightOpenEndedRangedPrimitiveByte.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RightOpenEndedRangedPrimitiveByte {
        @Property public void shouldHold(@InRange(min = "0") byte b) {
            assertThat(b, greaterThanOrEqualTo((byte) 0));
        }
    }

    @Test public void shrinkingPrimitivePositiveByte() {
        assertThat(testResult(ShrinkingPrimitivePositiveByte.class), failureCountIs(1));
        assertEquals(
            Byte.valueOf("3"),
            Iterables.getLast(ShrinkingPrimitivePositiveByte.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPrimitivePositiveByte {
        static List<Byte> values = new ArrayList<>();

        @Property public void shouldHold(@InRange(min = "3", max = "110") byte b) {
            values.add(b);

            fail();
        }
    }

    @Test public void shrinkingPrimitiveNegativeByte() {
        assertThat(testResult(ShrinkingPrimitiveNegativeByte.class), failureCountIs(1));

        assertEquals(
            Byte.valueOf("-13"),
            Iterables.getLast(ShrinkingPrimitiveNegativeByte.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPrimitiveNegativeByte {
        static List<Byte> values = new ArrayList<>();

        @Property public void shouldHold(@InRange(min = "-101", max = "-13") byte b) {
            values.add(b);

            fail();
        }
    }

    @Test public void shrinkingPrimitiveByteStraddlingZero() {
        assertThat(testResult(ShrinkingPrimitiveByteStraddlingZero.class), failureCountIs(1));

        assertEquals(
            Byte.valueOf("0"),
            Iterables.getLast(ShrinkingPrimitiveByteStraddlingZero.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPrimitiveByteStraddlingZero {
        static List<Byte> values = new ArrayList<>();

        @Property public void shouldHold(byte b) {
            values.add(b);

            fail();
        }
    }

    @Test public void wrapperByte() {
        assertThat(testResult(WrapperByte.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperByte {
        @Property public void shouldHold(Byte b) {
        }
    }

    @Test public void rangedWrapperByte() {
        assertThat(testResult(RangedWrapperByte.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedWrapperByte {
        @Property public void shouldHold(@InRange(min = "-3", max = "2") Byte b) {
            assertThat(b, allOf(greaterThanOrEqualTo((byte) -3), lessThanOrEqualTo((byte) 2)));
        }
    }

    @Test public void leftOpenEndedRangedWrapperByte() {
        assertThat(testResult(LeftOpenEndedRangedWrapperByte.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class LeftOpenEndedRangedWrapperByte {
        @Property public void shouldHold(@InRange(max = "0") Byte b) {
            assertThat(b, lessThanOrEqualTo((byte) 0));
        }
    }

    @Test public void rightOpenEndedRangedWrapperByte() {
        assertThat(testResult(RightOpenEndedRangedWrapperByte.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RightOpenEndedRangedWrapperByte {
        @Property public void shouldHold(@InRange(min = "0") Byte b) {
            assertThat(b, greaterThanOrEqualTo((byte) 0));
        }
    }

    @Test public void primitiveCharacter() {
        assertThat(testResult(PrimitiveCharacter.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveCharacter {
        @Property public void shouldHold(char ch) {
        }
    }

    @Test public void rangedPrimitiveCharacter() {
        assertThat(testResult(RangedPrimitiveCharacter.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedPrimitiveCharacter {
        @Property public void shouldHold(@InRange(minChar = 'a', maxChar = 'z') char ch) {
            assertThat(ch, allOf(greaterThanOrEqualTo('a'), lessThanOrEqualTo('z')));
        }
    }

    @Test public void leftOpenEndedRangedPrimitiveCharacter() {
        assertThat(testResult(LeftOpenEndedRangedPrimitiveCharacter.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class LeftOpenEndedRangedPrimitiveCharacter {
        @Property public void shouldHold(@InRange(maxChar = '\u00FF') char ch) {
            assertThat(ch, lessThanOrEqualTo('\u00FF'));
        }
    }

    @Test public void rightOpenEndedRangedPrimitiveCharacter() {
        assertThat(testResult(RightOpenEndedRangedPrimitiveCharacter.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RightOpenEndedRangedPrimitiveCharacter {
        @Property public void shouldHold(@InRange(minChar = '\uFF00') char ch) {
            assertThat(ch, greaterThanOrEqualTo('\uFF00'));
        }
    }

    @Test public void shrinkingPrimitiveCharacter() {
        assertThat(testResult(ShrinkingPrimitiveCharacter.class), failureCountIs(1));

        assumeThat(ShrinkingPrimitiveCharacter.values.size(), greaterThan(1));
        assertEquals(
            Character.valueOf(' '),
            Iterables.getLast(ShrinkingPrimitiveCharacter.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPrimitiveCharacter {
        static List<Character> values = new ArrayList<>();

        @Property public void shouldHold(@InRange(minChar = ' ', maxChar = '\u00FF') char ch) {
            values.add(ch);

            fail();
        }
    }

    @Test public void wrapperCharacter() {
        assertThat(testResult(WrapperCharacter.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperCharacter {
        @Property public void shouldHold(Character ch) {
        }
    }

    @Test public void rangedWrapperCharacter() {
        assertThat(testResult(RangedWrapperCharacter.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedWrapperCharacter {
        @Property public void shouldHold(@InRange(min = "0", max = "9") Character ch) {
            assertThat(ch, allOf(greaterThanOrEqualTo('0'), lessThanOrEqualTo('9')));
        }
    }

    @Test public void leftOpenEndedRangedWrapperCharacter() {
        assertThat(testResult(LeftOpenEndedRangedWrapperCharacter.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class LeftOpenEndedRangedWrapperCharacter {
        @Property public void shouldHold(@InRange(max = "\u00FF") Character ch) {
            assertThat(ch, lessThanOrEqualTo('\u00FF'));
        }
    }

    @Test public void rightOpenEndedRangedWrapperCharacter() {
        assertThat(testResult(RightOpenEndedRangedWrapperCharacter.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RightOpenEndedRangedWrapperCharacter {
        @Property public void shouldHold(@InRange(min = "\uFF00") Character ch) {
            assertThat(ch, greaterThanOrEqualTo('\uFF00'));
        }
    }

    @Test public void primitiveDouble() {
        assertThat(testResult(PrimitiveDouble.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveDouble {
        @Property public void shouldHold(double d) {
            assertThat(d, greaterThanOrEqualTo(RangeAttributes.minDouble()));
            assertThat(d, lessThan(RangeAttributes.maxDouble()));
        }
    }

    @Test public void rangedPrimitiveDouble() {
        assertThat(testResult(RangedPrimitiveDouble.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedPrimitiveDouble {
        @Property public void shouldHold(@InRange(minDouble = -2.71, maxDouble = 3.14) double d) {
            assertThat(d, allOf(greaterThanOrEqualTo(-2.71), lessThan(3.14)));
        }
    }

    @Test public void leftOpenEndedRangedPrimitiveDouble() {
        assertThat(testResult(LeftOpenEndedRangedPrimitiveDouble.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class LeftOpenEndedRangedPrimitiveDouble {
        @Property public void shouldHold(@InRange(maxDouble = 3.14) double d) {
            assertThat(d, greaterThanOrEqualTo(RangeAttributes.minDouble()));
            assertThat(d, lessThan(3.14));
        }
    }

    @Test public void rightOpenEndedRangedPrimitiveDouble() {
        assertThat(testResult(RightOpenEndedRangedPrimitiveDouble.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RightOpenEndedRangedPrimitiveDouble {
        @Property public void shouldHold(@InRange(minDouble = -2.71) double d) {
            assertThat(d, greaterThanOrEqualTo(-2.71));
            assertThat(d, lessThan(RangeAttributes.maxDouble()));
        }
    }

    @Test public void wrapperDouble() {
        assertThat(testResult(WrapperDouble.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperDouble {
        @Property public void shouldHold(Double d) {
            assertThat(d, greaterThanOrEqualTo(RangeAttributes.minDouble()));
            assertThat(d, lessThan(RangeAttributes.maxDouble()));
        }
    }

    @Test public void rangedWrapperDouble() {
        assertThat(testResult(RangedWrapperDouble.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedWrapperDouble {
        @Property public void shouldHold(@InRange(min = "2.71", max = "3.14") Double d) {
            assertThat(d, allOf(greaterThanOrEqualTo(2.71), lessThan(3.14)));
        }
    }

    @Test public void leftOpenEndedRangedWrapperDouble() {
        assertThat(testResult(LeftOpenEndedRangedWrapperDouble.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class LeftOpenEndedRangedWrapperDouble {
        @Property public void shouldHold(@InRange(max = "3.14") Double d) {
            assertThat(d, greaterThanOrEqualTo(RangeAttributes.minDouble()));
            assertThat(d, lessThan(3.14));
        }
    }

    @Test public void rightOpenEndedRangedWrapperDouble() {
        assertThat(testResult(RightOpenEndedRangedWrapperDouble.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RightOpenEndedRangedWrapperDouble {
        @Property public void shouldHold(@InRange(min = "-2.71") Double d) {
            assertThat(d, greaterThanOrEqualTo(-2.71));
            assertThat(d, lessThan(RangeAttributes.maxDouble()));
        }
    }

    @Test public void shrinkingPrimitivePositiveDouble() {
        assertThat(testResult(ShrinkingPrimitivePositiveDouble.class), failureCountIs(1));
        assertEquals(
            Double.valueOf(555.123123123123),
            Iterables.getLast(ShrinkingPrimitivePositiveDouble.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPrimitivePositiveDouble {
        static List<Double> values = new ArrayList<>();

        @Property public void shouldHold(
            @InRange(minDouble = 555.123123123123, maxDouble = 11111.222222222) double d) {

            values.add(d);

            fail();
        }
    }

    @Test public void shrinkingPrimitiveNegativeDouble() {
        assertThat(testResult(ShrinkingPrimitiveNegativeDouble.class), failureCountIs(1));

        assertEquals(
            Double.valueOf(-777.012301230123),
            Iterables.getLast(ShrinkingPrimitiveNegativeDouble.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPrimitiveNegativeDouble {
        static List<Double> values = new ArrayList<>();

        @Property public void shouldHold(
            @InRange(minDouble = -4400.998877665544, maxDouble = -777.012301230123) double d) {

            values.add(d);

            fail();
        }
    }

    @Test public void shrinkingPrimitiveDoubleStraddlingZero() {
        assertThat(testResult(ShrinkingPrimitiveDoubleStraddlingZero.class), failureCountIs(1));

        assertEquals(
            Double.valueOf(0),
            Iterables.getLast(ShrinkingPrimitiveDoubleStraddlingZero.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPrimitiveDoubleStraddlingZero {
        static List<Double> values = new ArrayList<>();

        @Property public void shouldHold(double d) {
            values.add(d);

            fail();
        }
    }

    @Test public void primitiveFloat() {
        assertThat(testResult(PrimitiveFloat.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveFloat {
        @Property public void shouldHold(float f) {
            assertThat(f, greaterThanOrEqualTo(RangeAttributes.minFloat()));
            assertThat(f, lessThan(RangeAttributes.maxFloat()));
        }
    }

    @Test public void rangedPrimitiveFloat() {
        assertThat(testResult(RangedPrimitiveFloat.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedPrimitiveFloat {
        @Property public void shouldHold(@InRange(minFloat = -2.51234F, maxFloat = 9.23423F) float f) {
            assertThat(f, allOf(greaterThanOrEqualTo(-2.51234F), lessThan(9.23423F)));
        }
    }

    @Test public void leftOpenEndedRangedPrimitiveFloat() {
        assertThat(testResult(LeftOpenEndedRangedPrimitiveFloat.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class LeftOpenEndedRangedPrimitiveFloat {
        @Property public void shouldHold(@InRange(maxFloat = 3.14F) float f) {
            assertThat(f, greaterThanOrEqualTo(RangeAttributes.minFloat()));
            assertThat(f, lessThanOrEqualTo(3.14F));
        }
    }

    @Test public void rightOpenEndedRangedPrimitiveFloat() {
        assertThat(testResult(RightOpenEndedRangedPrimitiveFloat.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RightOpenEndedRangedPrimitiveFloat {
        @Property public void shouldHold(@InRange(minFloat = -2.71F) float f) {
            assertThat(f, greaterThanOrEqualTo(-2.71F));
            assertThat(f, lessThan(RangeAttributes.maxFloat()));
        }
    }

    @Test public void wrapperFloat() {
        assertThat(testResult(WrapperFloat.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperFloat {
        @Property public void shouldHold(Float f) {
            assertThat(f, greaterThanOrEqualTo(RangeAttributes.minFloat()));
            assertThat(f, lessThan(RangeAttributes.maxFloat()));
        }
    }

    @Test public void rangedWrapperFloat() {
        assertThat(testResult(RangedWrapperFloat.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedWrapperFloat {
        @Property public void shouldHold(@InRange(min = "-0.1234", max = "0.000123") Float f) {
            assertThat(f, allOf(greaterThanOrEqualTo(-0.1234F), lessThan(0.000123F)));
        }
    }

    @Test public void leftOpenEndedRangedWrapperFloat() {
        assertThat(testResult(LeftOpenEndedRangedWrapperFloat.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class LeftOpenEndedRangedWrapperFloat {
        @Property public void shouldHold(@InRange(max = "3.14") Float f) {
            assertThat(f, greaterThanOrEqualTo(RangeAttributes.minFloat()));
            assertThat(f, lessThanOrEqualTo(3.14F));
        }
    }

    @Test public void rightOpenEndedRangedWrapperFloat() {
        assertThat(testResult(RightOpenEndedRangedWrapperFloat.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RightOpenEndedRangedWrapperFloat {
        @Property public void shouldHold(@InRange(min = "-2.71") Float f) {
            assertThat(f, greaterThanOrEqualTo(-2.71F));
            assertThat(f, lessThan(RangeAttributes.maxFloat()));
        }
    }

    @Test public void shrinkingPrimitivePositiveFloat() {
        assertThat(testResult(ShrinkingPrimitivePositiveFloat.class), failureCountIs(1));
        assertEquals(
            Float.valueOf(5.123123F),
            Iterables.getLast(ShrinkingPrimitivePositiveFloat.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPrimitivePositiveFloat {
        static List<Float> values = new ArrayList<>();

        @Property public void shouldHold(@InRange(minFloat = 5.123123F, maxFloat = 111.2222F) float f) {
            values.add(f);

            fail();
        }
    }

    @Test public void shrinkingPrimitiveNegativeFloat() {
        assertThat(testResult(ShrinkingPrimitiveNegativeFloat.class), failureCountIs(1));

        assertEquals(
            Float.valueOf(-7.0123F),
            Iterables.getLast(ShrinkingPrimitiveNegativeFloat.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPrimitiveNegativeFloat {
        static List<Float> values = new ArrayList<>();

        @Property public void shouldHold(@InRange(minFloat = -400.998877F, maxFloat = -7.0123F) float f) {
            values.add(f);

            fail();
        }
    }

    @Test public void shrinkingPrimitiveFloatStraddlingZero() {
        assertThat(testResult(ShrinkingPrimitiveFloatStraddlingZero.class), failureCountIs(1));

        assertEquals(
            Float.valueOf(0),
            Iterables.getLast(ShrinkingPrimitiveFloatStraddlingZero.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPrimitiveFloatStraddlingZero {
        static List<Float> values = new ArrayList<>();

        @Property public void shouldHold(float f) {
            values.add(f);

            fail();
        }
    }

    @Test public void primitiveInteger() {
        assertThat(testResult(PrimitiveInteger.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveInteger {
        @Property public void shouldHold(int i) {
        }
    }

    @Test public void rangedPrimitiveInteger() {
        assertThat(testResult(RangedPrimitiveInteger.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedPrimitiveInteger {
        @Property public void shouldHold(@InRange(minInt = 2, maxInt = 10) int i) {
            assertThat(i, allOf(greaterThanOrEqualTo(2), lessThanOrEqualTo(10)));
        }
    }

    @Test public void leftOpenEndedRangedPrimitiveInteger() {
        assertThat(testResult(LeftOpenEndedRangedPrimitiveInteger.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class LeftOpenEndedRangedPrimitiveInteger {
        @Property public void shouldHold(@InRange(maxInt = 4) int i) {
            assertThat(i, lessThanOrEqualTo(4));
        }
    }

    @Test public void rightOpenEndedRangedPrimitiveInteger() {
        assertThat(testResult(RightOpenEndedRangedPrimitiveInteger.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RightOpenEndedRangedPrimitiveInteger {
        @Property public void shouldHold(@InRange(minInt = -2) int i) {
            assertThat(i, greaterThanOrEqualTo(-2));
        }
    }

    @Test public void shrinkingPrimitivePositiveInteger() {
        assertThat(testResult(ShrinkingPrimitivePositiveInteger.class), failureCountIs(1));
        assertEquals(
            Integer.valueOf(5),
            Iterables.getLast(ShrinkingPrimitivePositiveInteger.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPrimitivePositiveInteger {
        static List<Integer> values = new ArrayList<>();

        @Property public void shouldHold(@InRange(minInt = 5, maxInt = 111) int i) {
            values.add(i);

            fail();
        }
    }

    @Test public void shrinkingPrimitiveNegativeInteger() {
        assertThat(testResult(ShrinkingPrimitiveNegativeInteger.class), failureCountIs(1));

        assertEquals(
            Integer.valueOf(-7),
            Iterables.getLast(ShrinkingPrimitiveNegativeInteger.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPrimitiveNegativeInteger {
        static List<Integer> values = new ArrayList<>();

        @Property public void shouldHold(@InRange(minInt = -400, maxInt = -7) int i) {
            values.add(i);

            fail();
        }
    }

    @Test public void shrinkingPrimitiveIntegerStraddlingZero() {
        assertThat(testResult(ShrinkingPrimitiveIntegerStraddlingZero.class), failureCountIs(1));

        assertEquals(
            Integer.valueOf(0),
            Iterables.getLast(ShrinkingPrimitiveIntegerStraddlingZero.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPrimitiveIntegerStraddlingZero {
        static List<Integer> values = new ArrayList<>();

        @Property public void shouldHold(int i) {
            values.add(i);

            fail();
        }
    }

    @Test public void wrapperInteger() {
        assertThat(testResult(WrapperInteger.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperInteger {
        @Property public void shouldHold(Integer i) {
        }
    }

    @Test public void rangedWrapperInteger() {
        assertThat(testResult(RangedWrapperInteger.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedWrapperInteger {
        @Property public void shouldHold(@InRange(min = "-4", max = "3") Integer i) {
            assertThat(i, allOf(greaterThanOrEqualTo(-4), lessThanOrEqualTo(3)));
        }
    }

    @Test public void leftOpenEndedRangedWrapperInteger() {
        assertThat(testResult(LeftOpenEndedRangedWrapperInteger.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class LeftOpenEndedRangedWrapperInteger {
        @Property public void shouldHold(@InRange(max = "-55") Integer i) {
            assertThat(i, lessThanOrEqualTo(-55));
        }
    }

    @Test public void rightOpenEndedRangedWrapperInteger() {
        assertThat(testResult(RightOpenEndedRangedWrapperInteger.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RightOpenEndedRangedWrapperInteger {
        @Property public void shouldHold(@InRange(min = "62") Integer i) {
            assertThat(i, greaterThanOrEqualTo(62));
        }
    }

    @Test public void primitiveLong() {
        assertThat(testResult(PrimitiveLong.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveLong {
        @Property public void shouldHold(long ell) {
        }
    }

    @Test public void rangedPrimitiveLong() {
        assertThat(testResult(RangedPrimitiveLong.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedPrimitiveLong {
        @Property public void shouldHold(@InRange(minLong = -10L, maxLong = 20L) long ell) {
            assertThat(ell, allOf(greaterThanOrEqualTo(-10L), lessThanOrEqualTo(20L)));
        }
    }

    @Test public void leftOpenEndedRangedPrimitiveLong() {
        assertThat(testResult(LeftOpenEndedRangedPrimitiveLong.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class LeftOpenEndedRangedPrimitiveLong {
        @Property public void shouldHold(@InRange(maxLong = 4L) long ell) {
            assertThat(ell, lessThanOrEqualTo(4L));
        }
    }

    @Test public void rightOpenEndedRangedPrimitiveLong() {
        assertThat(testResult(RightOpenEndedRangedPrimitiveLong.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RightOpenEndedRangedPrimitiveLong {
        @Property public void shouldHold(@InRange(minLong = -2L) long ell) {
            assertThat(ell, greaterThanOrEqualTo(-2L));
        }
    }

    @Test public void shrinkingPrimitivePositiveLong() {
        assertThat(testResult(ShrinkingPrimitivePositiveLong.class), failureCountIs(1));
        assertEquals(
            Long.valueOf(2),
            Iterables.getLast(ShrinkingPrimitivePositiveLong.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPrimitivePositiveLong {
        static List<Long> values = new ArrayList<>();

        @Property public void shouldHold(@InRange(minLong = 2, maxLong = 100) long ell) {
            values.add(ell);

            fail();
        }
    }

    @Test public void shrinkingPrimitiveNegativeLong() {
        assertThat(testResult(ShrinkingPrimitiveNegativeLong.class), failureCountIs(1));

        assertEquals(
            Long.valueOf(-5),
            Iterables.getLast(ShrinkingPrimitiveNegativeLong.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPrimitiveNegativeLong {
        static List<Long> values = new ArrayList<>();

        @Property public void shouldHold(@InRange(minLong = -300, maxLong = -5) long ell) {
            values.add(ell);

            fail();
        }
    }

    @Test public void shrinkingPrimitiveLongStraddlingZero() {
        assertThat(testResult(ShrinkingPrimitiveLongStraddlingZero.class), failureCountIs(1));

        assertEquals(
            Long.valueOf(0),
            Iterables.getLast(ShrinkingPrimitiveLongStraddlingZero.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPrimitiveLongStraddlingZero {
        static List<Long> values = new ArrayList<>();

        @Property public void shouldHold(long ell) {
            values.add(ell);

            fail();
        }
    }

    @Test public void wrapperLong() {
        assertThat(testResult(WrapperLong.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperLong {
        @Property public void shouldHold(Long ell) {
        }
    }

    @Test public void rangedWrapperLong() {
        assertThat(testResult(RangedWrapperLong.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedWrapperLong {
        @Property public void shouldHold(@InRange(min = "-10", max = "20") Long ell) {
            assertThat(ell, allOf(greaterThanOrEqualTo(-10L), lessThanOrEqualTo(20L)));
        }
    }

    @Test public void leftOpenEndedRangedWrapperLong() {
        assertThat(testResult(LeftOpenEndedRangedWrapperLong.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class LeftOpenEndedRangedWrapperLong {
        @Property public void shouldHold(@InRange(max = "4") Long ell) {
            assertThat(ell, lessThanOrEqualTo(4L));
        }
    }

    @Test public void rightOpenEndedRangedWrapperLong() {
        assertThat(testResult(RightOpenEndedRangedWrapperLong.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RightOpenEndedRangedWrapperLong {
        @Property public void shouldHold(@InRange(min = "-2") long ell) {
            assertThat(ell, greaterThanOrEqualTo(-2L));
        }
    }

    @Test public void primitiveShort() {
        assertThat(testResult(PrimitiveShort.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveShort {
        @Property public void shouldHold(short s) {
        }
    }

    @Test public void rangedPrimitiveShort() {
        assertThat(testResult(RangedPrimitiveShort.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedPrimitiveShort {
        @Property public void shouldHold(@InRange(min = "35", max = "67") short s) {
            assertThat(s, allOf(greaterThanOrEqualTo((short) 35), lessThanOrEqualTo((short) 67)));
        }
    }

    @Test public void leftOpenEndedRangedPrimitiveShort() {
        assertThat(testResult(LeftOpenEndedRangedPrimitiveShort.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class LeftOpenEndedRangedPrimitiveShort {
        @Property public void shouldHold(@InRange(max = "4") short s) {
            assertThat(s, lessThanOrEqualTo((short) 4));
        }
    }

    @Test public void rightOpenEndedRangedPrimitiveShort() {
        assertThat(testResult(RightOpenEndedRangedPrimitiveShort.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RightOpenEndedRangedPrimitiveShort {
        @Property public void shouldHold(@InRange(min = "-2") short s) {
            assertThat(s, greaterThanOrEqualTo((short) -2));
        }
    }

    @Test public void shrinkingPrimitivePositiveShort() {
        assertThat(testResult(ShrinkingPrimitivePositiveShort.class), failureCountIs(1));
        assertEquals(
            Short.valueOf("4"),
            Iterables.getLast(ShrinkingPrimitivePositiveShort.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPrimitivePositiveShort {
        static List<Short> values = new ArrayList<>();

        @Property public void shouldHold(@InRange(min = "4", max = "1111") short sh) {
            values.add(sh);

            fail();
        }
    }

    @Test public void shrinkingPrimitiveNegativeShort() {
        assertThat(testResult(ShrinkingPrimitiveNegativeShort.class), failureCountIs(1));

        assertEquals(
            Short.valueOf("-9"),
            Iterables.getLast(ShrinkingPrimitiveNegativeShort.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPrimitiveNegativeShort {
        static List<Short> values = new ArrayList<>();

        @Property public void shouldHold(@InRange(min = "-4000", max = "-9") short sh) {
            values.add(sh);

            fail();
        }
    }

    @Test public void shrinkingPrimitiveShortStraddlingZero() {
        assertThat(testResult(ShrinkingPrimitiveShortStraddlingZero.class), failureCountIs(1));

        assertEquals(
            Short.valueOf("0"),
            Iterables.getLast(ShrinkingPrimitiveShortStraddlingZero.values));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPrimitiveShortStraddlingZero {
        static List<Short> values = new ArrayList<>();

        @Property public void shouldHold(short sh) {
            values.add(sh);

            fail();
        }
    }

    @Test public void wrapperShort() {
        assertThat(testResult(WrapperShort.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperShort {
        @Property public void shouldHold(Short s) {
        }
    }

    @Test public void rangedWrapperShort() {
        assertThat(testResult(RangedWrapperShort.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RangedWrapperShort {
        @Property public void shouldHold(@InRange(min = "-10", max = "0") Short s) {
            assertThat(s, allOf(greaterThanOrEqualTo((short) -10), lessThanOrEqualTo((short) 0)));
        }
    }

    @Test public void leftOpenEndedRangedWrapperShort() {
        assertThat(testResult(LeftOpenEndedRangedWrapperShort.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class LeftOpenEndedRangedWrapperShort {
        @Property public void shouldHold(@InRange(max = "42") Short s) {
            assertThat(s, lessThanOrEqualTo((short) 42));
        }
    }

    @Test public void rightOpenEndedRangedWrapperShort() {
        assertThat(testResult(RightOpenEndedRangedWrapperShort.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RightOpenEndedRangedWrapperShort {
        @Property public void shouldHold(@InRange(min = "-21") Short s) {
            assertThat(s, greaterThanOrEqualTo((short) -21));
        }
    }

    @Test public void valuesOfNotApplicableOnNonBooleanNonEnum() throws Exception {
        assertThat(
            testResult(ValuesOfOnInt.class),
            hasSingleFailureContaining(GeneratorConfigurationException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ValuesOfOnInt {
        @Property public void shouldHold(@ValuesOf int i) {
        }
    }

    @Test public void voidParameter() throws Exception {
        assertThat(testResult(VoidParameter.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), VoidParameter.iterations);
    }

    @RunWith(JUnitQuickcheck.class)
    public static class VoidParameter {
        static int iterations;

        @Property public void shouldHold(Void v) {
            ++iterations;
        }
    }
}

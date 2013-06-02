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

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.*;

import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.ValuesOf;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Annotations.*;
import static com.pholser.junit.quickcheck.internal.Reflection.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ForAllPrimitiveTheoryParameterTypesTest {
    @Test public void primitiveBoolean() {
        assertThat(testResult(PrimitiveBoolean.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class PrimitiveBoolean {
        @Theory public void shouldHold(@ForAll boolean b) {
        }
    }

    @Test public void wrapperBoolean() {
        assertThat(testResult(WrapperBoolean.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperBoolean {
        @Theory public void shouldHold(@ForAll Boolean b) {
        }
    }

    @Test public void primitiveBooleanWithValuesOf() {
        assertThat(testResult(PrimitiveBooleanWithValuesOf.class), isSuccessful());
        assertEquals(asList(false, true), PrimitiveBooleanWithValuesOf.values);
    }

    @RunWith(Theories.class)
    public static class PrimitiveBooleanWithValuesOf {
        static List<Boolean> values = new ArrayList<Boolean>();

        @Theory public void shouldHold(@ForAll @ValuesOf boolean b) {
            values.add(b);
        }
    }

    @Test public void wrapperBooleanWithValuesOf() {
        assertThat(testResult(WrapperBooleanWithValuesOf.class), isSuccessful());
        assertEquals(asList(false, true), WrapperBooleanWithValuesOf.values);
    }

    @RunWith(Theories.class)
    public static class WrapperBooleanWithValuesOf {
        static List<Boolean> values = new ArrayList<Boolean>();

        @Theory public void shouldHold(@ForAll @ValuesOf Boolean b) {
            values.add(b);
        }
    }

    @Test public void primitiveByte() {
        assertThat(testResult(PrimitiveByte.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class PrimitiveByte {
        @Theory public void shouldHold(@ForAll byte b) {
        }
    }

    @Test public void rangedPrimitiveByte() {
        assertThat(testResult(RangedPrimitiveByte.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedPrimitiveByte {
        @Theory public void shouldHold(@ForAll @InRange(minByte = -23, maxByte = 34) byte b) {
            assertThat(b, allOf(greaterThanOrEqualTo((byte) -23), lessThanOrEqualTo((byte) 34)));
        }
    }

    @Test public void leftOpenEndedRangedPrimitiveByte() {
        assertThat(testResult(LeftOpenEndedRangedPrimitiveByte.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class LeftOpenEndedRangedPrimitiveByte {
        @Theory public void shouldHold(@ForAll @InRange(maxByte = 0) byte b) {
            assertThat(b, lessThanOrEqualTo((byte) 0));
        }
    }

    @Test public void rightOpenEndedRangedPrimitiveByte() {
        assertThat(testResult(RightOpenEndedRangedPrimitiveByte.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RightOpenEndedRangedPrimitiveByte {
        @Theory public void shouldHold(@ForAll @InRange(minByte = 0) byte b) {
            assertThat(b, greaterThanOrEqualTo((byte) 0));
        }
    }

    @Test public void wrapperByte() {
        assertThat(testResult(WrapperByte.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperByte {
        @Theory public void shouldHold(@ForAll Byte b) {
        }
    }

    @Test public void rangedWrapperByte() {
        assertThat(testResult(RangedWrapperByte.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedWrapperByte {
        @Theory public void shouldHold(@ForAll @InRange(minByte = -3, maxByte = 2) Byte b) {
            assertThat(b, allOf(greaterThanOrEqualTo((byte) -3), lessThanOrEqualTo((byte) 2)));
        }
    }

    @Test public void leftOpenEndedRangedWrapperByte() {
        assertThat(testResult(LeftOpenEndedRangedWrapperByte.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class LeftOpenEndedRangedWrapperByte {
        @Theory public void shouldHold(@ForAll @InRange(maxByte = 0) Byte b) {
            assertThat(b, lessThanOrEqualTo((byte) 0));
        }
    }

    @Test public void rightOpenEndedRangedWrapperByte() {
        assertThat(testResult(RightOpenEndedRangedWrapperByte.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RightOpenEndedRangedWrapperByte {
        @Theory public void shouldHold(@ForAll @InRange(minByte = 0) Byte b) {
            assertThat(b, greaterThanOrEqualTo((byte) 0));
        }
    }

    @Test public void primitiveCharacter() {
        assertThat(testResult(PrimitiveCharacter.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class PrimitiveCharacter {
        @Theory public void shouldHold(@ForAll char ch) {
        }
    }

    @Test public void rangedPrimitiveCharacter() {
        assertThat(testResult(RangedPrimitiveCharacter.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedPrimitiveCharacter {
        @Theory public void shouldHold(@ForAll @InRange(minChar = 'a', maxChar = 'z') char ch) {
            assertThat(ch, allOf(greaterThanOrEqualTo('a'), lessThanOrEqualTo('z')));
        }
    }

    @Test public void leftOpenEndedRangedPrimitiveCharacter() {
        assertThat(testResult(LeftOpenEndedRangedPrimitiveCharacter.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class LeftOpenEndedRangedPrimitiveCharacter {
        @Theory public void shouldHold(@ForAll @InRange(maxChar = '\u00FF') char ch) {
            assertThat(ch, lessThanOrEqualTo('\u00FF'));
        }
    }

    @Test public void rightOpenEndedRangedPrimitiveCharacter() {
        assertThat(testResult(RightOpenEndedRangedPrimitiveCharacter.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RightOpenEndedRangedPrimitiveCharacter {
        @Theory public void shouldHold(@ForAll @InRange(minChar = '\uFF00') char ch) {
            assertThat(ch, greaterThanOrEqualTo('\uFF00'));
        }
    }

    @Test public void wrapperCharacter() {
        assertThat(testResult(WrapperCharacter.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperCharacter {
        @Theory public void shouldHold(@ForAll Character ch) {
        }
    }

    @Test public void rangedWrapperCharacter() {
        assertThat(testResult(RangedWrapperCharacter.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedWrapperCharacter {
        @Theory public void shouldHold(@ForAll @InRange(minChar = '0', maxChar = '9') Character ch) {
            assertThat(ch, allOf(greaterThanOrEqualTo('0'), lessThanOrEqualTo('9')));
        }
    }

    @Test public void leftOpenEndedRangedWrapperCharacter() {
        assertThat(testResult(LeftOpenEndedRangedWrapperCharacter.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class LeftOpenEndedRangedWrapperCharacter {
        @Theory public void shouldHold(@ForAll @InRange(maxChar = '\u00FF') Character ch) {
            assertThat(ch, lessThanOrEqualTo('\u00FF'));
        }
    }

    @Test public void rightOpenEndedRangedWrapperCharacter() {
        assertThat(testResult(RightOpenEndedRangedWrapperCharacter.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RightOpenEndedRangedWrapperCharacter {
        @Theory public void shouldHold(@ForAll @InRange(minChar = '\uFF00') Character ch) {
            assertThat(ch, greaterThanOrEqualTo('\uFF00'));
        }
    }

    @Test public void primitiveDouble() {
        assertThat(testResult(PrimitiveDouble.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class PrimitiveDouble {
        @Theory public void shouldHold(@ForAll double d) {
            assertThat(d, greaterThanOrEqualTo((Double) defaultValueOf(InRange.class, "minDouble")));
            assertThat(d, lessThan((Double) defaultValueOf(InRange.class, "maxDouble")));
        }
    }

    @Test public void rangedPrimitiveDouble() {
        assertThat(testResult(RangedPrimitiveDouble.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedPrimitiveDouble {
        @Theory public void shouldHold(@ForAll @InRange(minDouble = Math.E, maxDouble = Math.PI) double d) {
            assertThat(d, allOf(greaterThanOrEqualTo(Math.E), lessThan(Math.PI)));
        }
    }

    @Test public void wrapperDouble() {
        assertThat(testResult(WrapperDouble.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperDouble {
        @Theory public void shouldHold(@ForAll Double d) {
            assertThat(d, greaterThanOrEqualTo((Double) defaultValueOf(InRange.class, "minDouble")));
            assertThat(d, lessThan((Double) defaultValueOf(InRange.class, "maxDouble")));
        }
    }

    @Test public void rangedWrapperDouble() {
        assertThat(testResult(RangedWrapperDouble.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedWrapperDouble {
        @Theory public void shouldHold(@ForAll @InRange(minDouble = Math.E, maxDouble = Math.PI) Double d) {
            assertThat(d, allOf(greaterThanOrEqualTo(Math.E), lessThan(Math.PI)));
        }
    }

    @Test public void primitiveFloat() {
        assertThat(testResult(PrimitiveFloat.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class PrimitiveFloat {
        @Theory public void shouldHold(@ForAll float f) {
            assertThat(f, greaterThanOrEqualTo((Float) defaultValueOf(InRange.class, "minFloat")));
            assertThat(f, lessThan((Float) defaultValueOf(InRange.class, "maxFloat")));
        }
    }

    @Test public void rangedPrimitiveFloat() {
        assertThat(testResult(RangedPrimitiveFloat.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedPrimitiveFloat {
        @Theory public void shouldHold(@ForAll @InRange(minFloat = 2.51234F, maxFloat = 9.23423F) float f) {
            assertThat(f, allOf(greaterThanOrEqualTo(2.51234F), lessThan(9.23423F)));
        }
    }

    @Test public void wrapperFloat() {
        assertThat(testResult(WrapperFloat.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperFloat {
        @Theory public void shouldHold(@ForAll Float f) {
            assertThat(f, greaterThanOrEqualTo((Float) defaultValueOf(InRange.class, "minFloat")));
            assertThat(f, lessThan((Float) defaultValueOf(InRange.class, "maxFloat")));
        }
    }

    @Test public void rangedWrapperFloat() {
        assertThat(testResult(RangedWrapperFloat.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedWrapperFloat {
        @Theory public void shouldHold(@ForAll @InRange(minFloat = -0.1234F, maxFloat = 0.000123F) Float f) {
            assertThat(f, allOf(greaterThanOrEqualTo(-0.1234F), lessThan(0.000123F)));
        }
    }

    @Test public void primitiveInteger() {
        assertThat(testResult(PrimitiveInteger.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class PrimitiveInteger {
        @Theory public void shouldHold(@ForAll int i) {
        }
    }

    @Test public void rangedPrimitiveInteger() {
        assertThat(testResult(RangedPrimitiveInteger.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedPrimitiveInteger {
        @Theory public void shouldHold(@ForAll @InRange(minInt = 2, maxInt = 10) int i) {
            assertThat(i, allOf(greaterThanOrEqualTo(2), lessThanOrEqualTo(10)));
        }
    }

    @Test public void wrapperInteger() {
        assertThat(testResult(WrapperInteger.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperInteger {
        @Theory public void shouldHold(@ForAll Integer i) {
        }
    }

    @Test public void rangedWrapperInteger() {
        assertThat(testResult(RangedWrapperInteger.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedWrapperInteger {
        @Theory public void shouldHold(@ForAll @InRange(minInt = -4, maxInt = 3) Integer i) {
            assertThat(i, allOf(greaterThanOrEqualTo(-4), lessThanOrEqualTo(3)));
        }
    }

    @Test public void primitiveLong() {
        assertThat(testResult(PrimitiveLong.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class PrimitiveLong {
        @Theory public void shouldHold(@ForAll long ell) {
        }
    }

    @Test public void rangedPrimitiveLong() {
        assertThat(testResult(RangedPrimitiveLong.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedPrimitiveLong {
        @Theory public void shouldHold(@ForAll @InRange(minLong = -10, maxLong = 20) long ell) {
            assertThat(ell, allOf(greaterThanOrEqualTo(-10L), lessThanOrEqualTo(20L)));
        }
    }

    @Test public void wrapperLong() {
        assertThat(testResult(WrapperLong.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperLong {
        @Theory public void shouldHold(@ForAll Long ell) {
        }
    }

    @Test public void rangedWrapperLong() {
        assertThat(testResult(RangedWrapperLong.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedWrapperLong {
        @Theory public void shouldHold(@ForAll @InRange(minLong = -10, maxLong = 20) Long ell) {
            assertThat(ell, allOf(greaterThanOrEqualTo(-10L), lessThanOrEqualTo(20L)));
        }
    }

    @Test public void primitiveShort() {
        assertThat(testResult(PrimitiveShort.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class PrimitiveShort {
        @Theory public void shouldHold(@ForAll short s) {
        }
    }

    @Test public void rangedPrimitiveShort() {
        assertThat(testResult(RangedPrimitiveShort.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedPrimitiveShort {
        @Theory public void shouldHold(@ForAll @InRange(minShort = 35, maxShort = 67) short s) {
            assertThat(s, allOf(greaterThanOrEqualTo((short) 35), lessThanOrEqualTo((short) 67)));
        }
    }

    @Test public void wrapperShort() {
        assertThat(testResult(WrapperShort.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperShort {
        @Theory public void shouldHold(@ForAll Short s) {
        }
    }

    @Test public void rangedWrapperShort() {
        assertThat(testResult(RangedWrapperShort.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class RangedWrapperShort {
        @Theory public void shouldHold(@ForAll @InRange(minShort = -10, maxShort = 0) Short s) {
            assertThat(s, allOf(greaterThanOrEqualTo((short) -10), lessThanOrEqualTo((short) 0)));
        }
    }

    @Test public void valuesOfHasNoEffectOnNonBooleanNonEnum() throws Exception {
        assertThat(testResult(ValuesOfOnInt.class), isSuccessful());
        assertEquals(defaultSampleSize(), ValuesOfOnInt.iterations);
    }

    @RunWith(Theories.class)
    public static class ValuesOfOnInt {
        static int iterations;

        @Theory public void shouldHold(@ForAll @ValuesOf int i) {
            ++iterations;
        }
    }
}

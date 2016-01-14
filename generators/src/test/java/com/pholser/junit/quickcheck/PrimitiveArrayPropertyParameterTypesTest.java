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

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class PrimitiveArrayPropertyParameterTypesTest {
    @Test public void primitiveBooleanArray() {
        assertThat(testResult(PrimitiveBooleanArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveBooleanArray {
        @Property public void shouldHold(boolean[] b) {
        }
    }

    @Test public void wrapperBooleanArray() {
        assertThat(testResult(WrapperBooleanArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperBooleanArray {
        @Property public void shouldHold(Boolean[] b) {
        }
    }

    @Test public void primitiveByteArray() {
        assertThat(testResult(PrimitiveByteArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveByteArray {
        @Property public void shouldHold(byte[] b) {
        }
    }

    @Test public void wrapperByteArray() {
        assertThat(testResult(WrapperByteArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperByteArray {
        @Property public void shouldHold(Byte[] b) {
        }
    }

    @Test public void primitiveCharacterArray() {
        assertThat(testResult(PrimitiveCharacterArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveCharacterArray {
        @Property public void shouldHold(char[] ch) {
        }
    }

    @Test public void wrapperCharacterArray() {
        assertThat(testResult(WrapperCharacterArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperCharacterArray {
        @Property public void shouldHold(Character[] ch) {
        }
    }

    @Test public void primitiveDoubleArray() {
        assertThat(testResult(PrimitiveDoubleArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveDoubleArray {
        @Property public void shouldHold(double[] d) {
        }
    }

    @Test public void wrapperDoubleArray() {
        assertThat(testResult(WrapperDoubleArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperDoubleArray {
        @Property public void shouldHold(Double[] d) {
        }
    }

    @Test public void primitiveFloatArray() {
        assertThat(testResult(PrimitiveFloatArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveFloatArray {
        @Property public void shouldHold(float[] f) {
        }
    }

    @Test public void wrapperFloatArray() {
        assertThat(testResult(WrapperFloatArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperFloatArray {
        @Property public void shouldHold(Float[] f) {
        }
    }

    @Test public void primitiveIntegerArray() {
        assertThat(testResult(PrimitiveIntegerArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveIntegerArray {
        @Property public void shouldHold(int[] i) {
        }
    }

    @Test public void shrinkingPrimitiveIntegerArray() {
        assertThat(testResult(ShrinkingPrimitiveIntegerArray.class), failureCountIs(1));
        assertThat(ShrinkingPrimitiveIntegerArray.failed.length, greaterThan(5));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingPrimitiveIntegerArray {
        static int[] failed;

        @Property public void shouldHold(int[] i) {
            assumeThat(i.length, greaterThan(5));

            failed = i;

            fail();
        }
    }

    @Test public void wrapperIntegerArray() {
        assertThat(testResult(WrapperIntegerArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperIntegerArray {
        @Property public void shouldHold(Integer[] i) {
        }
    }

    @Test public void primitiveLongArray() {
        assertThat(testResult(PrimitiveLongArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveLongArray {
        @Property public void shouldHold(long[] ell) {
        }
    }

    @Test public void wrapperLongArray() {
        assertThat(testResult(WrapperLongArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperLongArray {
        @Property public void shouldHold(Long[] ell) {
        }
    }

    @Test public void primitiveShortArray() {
        assertThat(testResult(PrimitiveShortArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveShortArray {
        @Property public void shouldHold(short[] s) {
        }
    }

    @Test public void wrapperShortArray() {
        assertThat(testResult(WrapperShortArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperShortArray {
        @Property public void shouldHold(Short[] s) {
        }
    }
}

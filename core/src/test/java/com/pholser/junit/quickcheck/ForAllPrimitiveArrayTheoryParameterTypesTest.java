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

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ForAllPrimitiveArrayTheoryParameterTypesTest {
    @Test public void primitiveBooleanArray() {
        assertThat(testResult(PrimitiveBooleanArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class PrimitiveBooleanArray {
        @Theory public void shouldHold(@ForAll boolean[] b) {
        }
    }

    @Test public void wrapperBooleanArray() {
        assertThat(testResult(WrapperBooleanArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperBooleanArray {
        @Theory public void shouldHold(@ForAll Boolean[] b) {
        }
    }

    @Test public void primitiveByteArray() {
        assertThat(testResult(PrimitiveByteArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class PrimitiveByteArray {
        @Theory public void shouldHold(@ForAll byte[] b) {
        }
    }

    @Test public void wrapperByteArray() {
        assertThat(testResult(WrapperByteArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperByteArray {
        @Theory public void shouldHold(@ForAll Byte[] b) {
        }
    }

    @Test public void primitiveCharacterArray() {
        assertThat(testResult(PrimitiveCharacterArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class PrimitiveCharacterArray {
        @Theory public void shouldHold(@ForAll char[] ch) {
        }
    }

    @Test public void wrapperCharacterArray() {
        assertThat(testResult(WrapperCharacterArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperCharacterArray {
        @Theory public void shouldHold(@ForAll Character[] ch) {
        }
    }

    @Test public void primitiveDoubleArray() {
        assertThat(testResult(PrimitiveDoubleArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class PrimitiveDoubleArray {
        @Theory public void shouldHold(@ForAll double[] d) {
        }
    }

    @Test public void wrapperDoubleArray() {
        assertThat(testResult(WrapperDoubleArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperDoubleArray {
        @Theory public void shouldHold(@ForAll Double[] d) {
        }
    }

    @Test public void primitiveFloatArray() {
        assertThat(testResult(PrimitiveFloatArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class PrimitiveFloatArray {
        @Theory public void shouldHold(@ForAll float[] f) {
        }
    }

    @Test public void wrapperFloatArray() {
        assertThat(testResult(WrapperFloatArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperFloatArray {
        @Theory public void shouldHold(@ForAll Float[] f) {
        }
    }

    @Test public void primitiveIntegerArray() {
        assertThat(testResult(PrimitiveIntegerArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class PrimitiveIntegerArray {
        @Theory public void shouldHold(@ForAll int[] i) {
        }
    }

    @Test public void wrapperIntegerArray() {
        assertThat(testResult(WrapperIntegerArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperIntegerArray {
        @Theory public void shouldHold(@ForAll Integer[] i) {
        }
    }

    @Test public void primitiveLongArray() {
        assertThat(testResult(PrimitiveLongArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class PrimitiveLongArray {
        @Theory public void shouldHold(@ForAll long[] ell) {
        }
    }

    @Test public void wrapperLongArray() {
        assertThat(testResult(WrapperLongArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperLongArray {
        @Theory public void shouldHold(@ForAll Long[] ell) {
        }
    }

    @Test public void primitiveShortArray() {
        assertThat(testResult(PrimitiveShortArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class PrimitiveShortArray {
        @Theory public void shouldHold(@ForAll short[] s) {
        }
    }

    @Test public void wrapperShortArray() {
        assertThat(testResult(WrapperShortArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperShortArray {
        @Theory public void shouldHold(@ForAll Short[] s) {
        }
    }
}

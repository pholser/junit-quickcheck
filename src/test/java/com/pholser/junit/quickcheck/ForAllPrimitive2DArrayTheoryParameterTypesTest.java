/*
 The MIT License

 Copyright (c) 2010-2011 Paul R. Holser, Jr.

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
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ForAllPrimitive2DArrayTheoryParameterTypesTest {
    @Test
    public void primitive2DBooleanArray() {
        assertThat(testResult(Primitive2DBooleanArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive2DBooleanArray {
        @Theory
        public void shouldHold(@ForAll(sampleSize = 10) boolean[][] b) {
        }
    }

    @Test
    public void wrapperBooleanArray() {
        assertThat(testResult(WrapperBooleanArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperBooleanArray {
        @Theory
        public void shouldHold(@ForAll(sampleSize = 10) Boolean[][] b) {
        }
    }

    @Test
    public void primitive2DByteArray() {
        assertThat(testResult(Primitive2DByteArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive2DByteArray {
        @Theory
        public void shouldHold(@ForAll(sampleSize = 10) byte[][] b) {
        }
    }

    @Test
    public void wrapperByteArray() {
        assertThat(testResult(WrapperByteArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperByteArray {
        @Theory
        public void shouldHold(@ForAll(sampleSize = 10) Byte[][] b) {
        }
    }

    @Test
    public void primitive2DCharacterArray() {
        assertThat(testResult(Primitive2DCharacterArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive2DCharacterArray {
        @Theory
        public void shouldHold(@ForAll(sampleSize = 10) char[][] ch) {
        }
    }

    @Test
    public void wrapperCharacterArray() {
        assertThat(testResult(WrapperCharacterArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperCharacterArray {
        @Theory
        public void shouldHold(@ForAll(sampleSize = 10) Character[][] ch) {
        }
    }

    @Test
    public void primitive2DDoubleArray() {
        assertThat(testResult(Primitive2DDoubleArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive2DDoubleArray {
        @Theory
        public void shouldHold(@ForAll(sampleSize = 10) double[][] d) {
        }
    }

    @Test
    public void wrapperDoubleArray() {
        assertThat(testResult(WrapperDoubleArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperDoubleArray {
        @Theory
        public void shouldHold(@ForAll(sampleSize = 10) Double[][] d) {
        }
    }

    @Test
    public void primitive2DFloatArray() {
        assertThat(testResult(Primitive2DFloatArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive2DFloatArray {
        @Theory
        public void shouldHold(@ForAll(sampleSize = 10) float[][] f) {
        }
    }

    @Test
    public void wrapperFloatArray() {
        assertThat(testResult(WrapperFloatArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperFloatArray {
        @Theory
        public void shouldHold(@ForAll(sampleSize = 10) Float[][] f) {
        }
    }

    @Test
    public void primitive2DIntegerArray() {
        assertThat(testResult(Primitive2DIntegerArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive2DIntegerArray {
        @Theory
        public void shouldHold(@ForAll(sampleSize = 10) int[][] i) {
        }
    }

    @Test
    public void wrapperIntegerArray() {
        assertThat(testResult(WrapperIntegerArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperIntegerArray {
        @Theory
        public void shouldHold(@ForAll(sampleSize = 10) Integer[][] i) {
        }
    }

    @Test
    public void primitive2DLongArray() {
        assertThat(testResult(Primitive2DLongArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive2DLongArray {
        @Theory
        public void shouldHold(@ForAll(sampleSize = 10) long[][] ell) {
        }
    }

    @Test
    public void wrapperLongArray() {
        assertThat(testResult(WrapperLongArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperLongArray {
        @Theory
        public void shouldHold(@ForAll(sampleSize = 10) Long[][] ell) {
        }
    }

    @Test
    public void primitive2DShortArray() {
        assertThat(testResult(Primitive2DShortArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive2DShortArray {
        @Theory
        public void shouldHold(@ForAll(sampleSize = 10) short[][] s) {
        }
    }

    @Test
    public void wrapperShortArray() {
        assertThat(testResult(WrapperShortArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperShortArray {
        @Theory
        public void shouldHold(@ForAll(sampleSize = 10) Short[][] s) {
        }
    }
}

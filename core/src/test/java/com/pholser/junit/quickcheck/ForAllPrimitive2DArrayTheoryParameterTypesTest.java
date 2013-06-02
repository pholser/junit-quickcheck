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

public class ForAllPrimitive2DArrayTheoryParameterTypesTest {
    @Test public void primitive2DBooleanArray() {
        assertThat(testResult(Primitive2DBooleanArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive2DBooleanArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 10) boolean[][] b) {
        }
    }

    @Test public void wrapper2DBooleanArray() {
        assertThat(testResult(Wrapper2DBooleanArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Wrapper2DBooleanArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 10) Boolean[][] b) {
        }
    }

    @Test public void primitive2DByteArray() {
        assertThat(testResult(Primitive2DByteArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive2DByteArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 10) byte[][] b) {
        }
    }

    @Test public void wrapper2DByteArray() {
        assertThat(testResult(Wrapper2DByteArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Wrapper2DByteArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 10) Byte[][] b) {
        }
    }

    @Test public void primitive2DCharacterArray() {
        assertThat(testResult(Primitive2DCharacterArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive2DCharacterArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 10) char[][] ch) {
        }
    }

    @Test public void wrapper2DCharacterArray() {
        assertThat(testResult(Wrapper2DCharacterArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Wrapper2DCharacterArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 10) Character[][] ch) {
        }
    }

    @Test public void primitive2DDoubleArray() {
        assertThat(testResult(Primitive2DDoubleArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive2DDoubleArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 10) double[][] d) {
        }
    }

    @Test public void wrapperDoubleArray() {
        assertThat(testResult(WrapperDoubleArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperDoubleArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 10) Double[][] d) {
        }
    }

    @Test public void primitive2DFloatArray() {
        assertThat(testResult(Primitive2DFloatArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive2DFloatArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 10) float[][] f) {
        }
    }

    @Test public void wrapper2DFloatArray() {
        assertThat(testResult(Wrapper2DFloatArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Wrapper2DFloatArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 10) Float[][] f) {
        }
    }

    @Test public void primitive2DIntegerArray() {
        assertThat(testResult(Primitive2DIntegerArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive2DIntegerArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 10) int[][] i) {
        }
    }

    @Test public void wrapper2DIntegerArray() {
        assertThat(testResult(Wrapper2DIntegerArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Wrapper2DIntegerArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 10) Integer[][] i) {
        }
    }

    @Test public void primitive2DLongArray() {
        assertThat(testResult(Primitive2DLongArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive2DLongArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 10) long[][] ell) {
        }
    }

    @Test public void wrapper2DLongArray() {
        assertThat(testResult(Wrapper2DLongArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Wrapper2DLongArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 10) Long[][] ell) {
        }
    }

    @Test public void primitive2DShortArray() {
        assertThat(testResult(Primitive2DShortArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive2DShortArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 10) short[][] s) {
        }
    }

    @Test public void wrapper2DShortArray() {
        assertThat(testResult(Wrapper2DShortArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Wrapper2DShortArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 10) Short[][] s) {
        }
    }
}

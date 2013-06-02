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

public class ForAllPrimitive3DArrayTheoryParameterTypesTest {
    @Test public void primitive3DBooleanArray() {
        assertThat(testResult(Primitive3DBooleanArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive3DBooleanArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 2) boolean[][][] b) {
        }
    }

    @Test public void wrapperBooleanArray() {
        assertThat(testResult(WrapperBooleanArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperBooleanArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 2) Boolean[][][] b) {
        }
    }

    @Test public void primitive3DByteArray() {
        assertThat(testResult(Primitive3DByteArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive3DByteArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 2) byte[][][] b) {
        }
    }

    @Test public void wrapperByteArray() {
        assertThat(testResult(WrapperByteArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperByteArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 2) Byte[][][] b) {
        }
    }

    @Test public void primitive3DCharacterArray() {
        assertThat(testResult(Primitive3DCharacterArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive3DCharacterArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 2) char[][][] ch) {
        }
    }

    @Test public void wrapperCharacterArray() {
        assertThat(testResult(WrapperCharacterArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperCharacterArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 2) Character[][][] ch) {
        }
    }

    @Test public void primitive3DDoubleArray() {
        assertThat(testResult(Primitive3DDoubleArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive3DDoubleArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 2) double[][][] d) {
        }
    }

    @Test public void wrapperDoubleArray() {
        assertThat(testResult(WrapperDoubleArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperDoubleArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 2) Double[][][] d) {
        }
    }

    @Test public void primitive3DFloatArray() {
        assertThat(testResult(Primitive3DFloatArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive3DFloatArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 2) float[][][] f) {
        }
    }

    @Test public void wrapperFloatArray() {
        assertThat(testResult(WrapperFloatArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperFloatArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 2) Float[][][] f) {
        }
    }

    @Test public void primitive3DIntegerArray() {
        assertThat(testResult(Primitive3DIntegerArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive3DIntegerArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 2) int[][][] i) {
        }
    }

    @Test public void wrapperIntegerArray() {
        assertThat(testResult(WrapperIntegerArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperIntegerArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 2) Integer[][][] i) {
        }
    }

    @Test public void primitive3DLongArray() {
        assertThat(testResult(Primitive3DLongArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive3DLongArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 2) long[][][] ell) {
        }
    }

    @Test public void wrapperLongArray() {
        assertThat(testResult(WrapperLongArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperLongArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 2) Long[][][] ell) {
        }
    }

    @Test public void primitive3DShortArray() {
        assertThat(testResult(Primitive3DShortArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class Primitive3DShortArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 2) short[][][] s) {
        }
    }

    @Test public void wrapperShortArray() {
        assertThat(testResult(WrapperShortArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WrapperShortArray {
        @Theory public void shouldHold(@ForAll(sampleSize = 2) Short[][][] s) {
        }
    }
}

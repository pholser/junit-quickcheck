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

import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class Primitive2DArrayPropertyParameterTypesTest {
    @Test public void primitive2DBooleanArray() {
        assertThat(testResult(Primitive2DBooleanArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Primitive2DBooleanArray {
        @Property(trials = 10) public void shouldHold(boolean[][] b) {
        }
    }

    @Test public void wrapper2DBooleanArray() {
        assertThat(testResult(Wrapper2DBooleanArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Wrapper2DBooleanArray {
        @Property(trials = 10) public void shouldHold(Boolean[][] b) {
        }
    }

    @Test public void primitive2DByteArray() {
        assertThat(testResult(Primitive2DByteArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Primitive2DByteArray {
        @Property(trials = 10) public void shouldHold(byte[][] b) {
        }
    }

    @Test public void wrapper2DByteArray() {
        assertThat(testResult(Wrapper2DByteArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Wrapper2DByteArray {
        @Property(trials = 10) public void shouldHold(Byte[][] b) {
        }
    }

    @Test public void primitive2DCharacterArray() {
        assertThat(testResult(Primitive2DCharacterArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Primitive2DCharacterArray {
        @Property(trials = 10) public void shouldHold(char[][] ch) {
        }
    }

    @Test public void wrapper2DCharacterArray() {
        assertThat(testResult(Wrapper2DCharacterArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Wrapper2DCharacterArray {
        @Property(trials = 10) public void shouldHold(Character[][] ch) {
        }
    }

    @Test public void primitive2DDoubleArray() {
        assertThat(testResult(Primitive2DDoubleArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Primitive2DDoubleArray {
        @Property(trials = 10) public void shouldHold(double[][] d) {
        }
    }

    @Test public void wrapperDoubleArray() {
        assertThat(testResult(WrapperDoubleArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperDoubleArray {
        @Property(trials = 10) public void shouldHold(Double[][] d) {
        }
    }

    @Test public void primitive2DFloatArray() {
        assertThat(testResult(Primitive2DFloatArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Primitive2DFloatArray {
        @Property(trials = 10) public void shouldHold(float[][] f) {
        }
    }

    @Test public void wrapper2DFloatArray() {
        assertThat(testResult(Wrapper2DFloatArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Wrapper2DFloatArray {
        @Property(trials = 10) public void shouldHold(Float[][] f) {
        }
    }

    @Test public void primitive2DIntegerArray() {
        assertThat(testResult(Primitive2DIntegerArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Primitive2DIntegerArray {
        @Property(trials = 10) public void shouldHold(int[][] i) {
        }
    }

    @Test public void wrapper2DIntegerArray() {
        assertThat(testResult(Wrapper2DIntegerArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Wrapper2DIntegerArray {
        @Property(trials = 10) public void shouldHold(Integer[][] i) {
        }
    }

    @Test public void primitive2DLongArray() {
        assertThat(testResult(Primitive2DLongArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Primitive2DLongArray {
        @Property(trials = 10) public void shouldHold(long[][] ell) {
        }
    }

    @Test public void wrapper2DLongArray() {
        assertThat(testResult(Wrapper2DLongArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Wrapper2DLongArray {
        @Property(trials = 10) public void shouldHold(Long[][] ell) {
        }
    }

    @Test public void primitive2DShortArray() {
        assertThat(testResult(Primitive2DShortArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Primitive2DShortArray {
        @Property(trials = 10) public void shouldHold(short[][] s) {
        }
    }

    @Test public void wrapper2DShortArray() {
        assertThat(testResult(Wrapper2DShortArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Wrapper2DShortArray {
        @Property(trials = 10) public void shouldHold(Short[][] s) {
        }
    }
}

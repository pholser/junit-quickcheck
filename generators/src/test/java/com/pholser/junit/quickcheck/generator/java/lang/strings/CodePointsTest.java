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

package com.pholser.junit.quickcheck.generator.java.lang.strings;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import static org.junit.Assert.assertEquals;
import static org.junit.rules.ExpectedException.*;

public class CodePointsTest {
    @Rule public final ExpectedException thrown = none();

    @Test public void versusCharsetThatDoesNotSupportEncoding() {
        Charset noEncoding;

        try {
            noEncoding = Charset.forName("ISO-2022-CN");
        } catch (UnsupportedCharsetException testNotValid) {
            return;
        }

        thrown.expect(IllegalArgumentException.class);

        CodePoints.forCharset(noEncoding);
    }

    @Test public void middlingTree() {
        CodePoints points = new CodePoints();
        points.add(new CodePoints.CodePointRange(20, 21, 0));
        points.add(new CodePoints.CodePointRange(30, 34, 2));
        points.add(new CodePoints.CodePointRange(50, 55, 7));
        points.add(new CodePoints.CodePointRange(60, 62, 13));
        points.add(new CodePoints.CodePointRange(70, 73, 16));
        points.add(new CodePoints.CodePointRange(80, 87, 20));

        assertEquals(28, points.size());
        assertEquals(20, points.at(0));
        assertEquals(21, points.at(1));
        assertEquals(30, points.at(2));
        assertEquals(31, points.at(3));
        assertEquals(32, points.at(4));
        assertEquals(33, points.at(5));
        assertEquals(34, points.at(6));
        assertEquals(50, points.at(7));
        assertEquals(51, points.at(8));
        assertEquals(52, points.at(9));
        assertEquals(53, points.at(10));
        assertEquals(54, points.at(11));
        assertEquals(55, points.at(12));
        assertEquals(60, points.at(13));
        assertEquals(61, points.at(14));
        assertEquals(62, points.at(15));
        assertEquals(70, points.at(16));
        assertEquals(71, points.at(17));
        assertEquals(72, points.at(18));
        assertEquals(73, points.at(19));
        assertEquals(80, points.at(20));
        assertEquals(81, points.at(21));
        assertEquals(82, points.at(22));
        assertEquals(83, points.at(23));
        assertEquals(84, points.at(24));
        assertEquals(85, points.at(25));
        assertEquals(86, points.at(26));
        assertEquals(87, points.at(27));
    }

    @Test public void lowIndex() {
        thrown.expect(IndexOutOfBoundsException.class);

        new CodePoints().at(-1);
    }

    @Test public void highIndex() {
        thrown.expect(IndexOutOfBoundsException.class);

        new CodePoints().at(0);
    }

    @Test public void sizeOfEmpty() {
        assertEquals(0, new CodePoints().size());
    }
}

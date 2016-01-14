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

import com.pholser.junit.quickcheck.generator.java.lang.strings.CodePoints.CodePointRange;
import org.junit.Test;

import static org.junit.Assert.*;

public class CodePointRangeTest {
    @Test(expected = IllegalArgumentException.class)
    public void illegalRange() {
        new CodePointRange(54, 53, 0);
    }

    @Test public void rangeOfOne() {
        CodePointRange range = new CodePointRange(55, 55, 0);

        assertEquals(1, range.size());
        assertFalse(range.contains(54));
        assertTrue(range.contains(55));
        assertFalse(range.contains(56));
    }

    @Test public void largerRange() {
        CodePointRange range = new CodePointRange(55, 58, 0);

        assertEquals(4, range.size());
        assertFalse(range.contains(54));
        assertTrue(range.contains(55));
        assertTrue(range.contains(56));
        assertTrue(range.contains(57));
        assertTrue(range.contains(58));
        assertFalse(range.contains(59));
    }
}

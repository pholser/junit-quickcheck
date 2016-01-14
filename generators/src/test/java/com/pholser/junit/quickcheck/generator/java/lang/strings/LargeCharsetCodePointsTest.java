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

import com.google.common.base.Predicate;
import com.pholser.junit.quickcheck.LongRunning;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.*;
import static java.nio.charset.Charset.*;
import static org.junit.Assert.*;

@Category(LongRunning.class)
@RunWith(Parameterized.class)
public class LargeCharsetCodePointsTest {
    private Charset charset;
    private CodePoints points;

    public LargeCharsetCodePointsTest(Charset charset) {
        this.charset = charset;
        points = CodePoints.forCharset(charset);
    }

    @Parameterized.Parameters(name = "charset {0}")
    public static Iterable<Charset> data() {
        return availableCharsets().values().stream()
            .filter(Charset::canEncode)
            .collect(Collectors.toList());
    }

    @Test public void nthElement() {
        CharsetEncoder encoder = charset.newEncoder();
        int[] buffer = new int[1];

        for (int index = 0, i = 0; i < 0x10FFFF; ++i) {
            buffer[0] = i;
            if (encoder.canEncode(new String(buffer, 0, 1))) {
                String message = String.format("The %d'th character in %s should be %06x", index, charset.name(), i);
                assertEquals(message, i, points.at(index));
                ++index;
            }
        }
    }
}

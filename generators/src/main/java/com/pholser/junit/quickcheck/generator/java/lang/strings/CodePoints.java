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

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * Maps ordinal values to corresponding Unicode code points in a
 * {@link java.nio.charset.Charset}.
 */
public class CodePoints {
    private static final Map<Charset, CodePoints> ENCODABLES = new HashMap<>();

    private final List<CodePointRange> ranges;

    CodePoints() {
        ranges = new ArrayList<>();
    }

    /**
     * @param index index to look up
     * @return this code point set's {@code index}'th code point
     * @throws IndexOutOfBoundsException if there is no such code point
     */
    public int at(int index) {
        if (index < 0)
            throw new IndexOutOfBoundsException("illegal negative index: " + index);

        int min = 0;
        int max = ranges.size() - 1;

        while (min <= max) {
            int midpoint = min + ((max - min) / 2);
            CodePointRange current = ranges.get(midpoint);

            if (index >= current.previousCount && index < current.previousCount + current.size())
                return current.low + index - current.previousCount;
            else if (index < current.previousCount)
                max = midpoint - 1;
            else
                min = midpoint + 1;
        }

        throw new IndexOutOfBoundsException(String.valueOf(index));
    }

    /**
     * @return how many code points are in this code point set
     */
    public int size() {
        if (ranges.isEmpty())
            return 0;

        CodePointRange last = ranges.get(ranges.size() - 1);
        return last.previousCount + last.size();
    }

    /**
     * @param codePoint a code point
     * @return whether this code point set contains the given code point
     */
    public boolean contains(int codePoint) {
        return ranges.stream().anyMatch(r -> r.contains(codePoint));
    }

    /**
     * Gives a set of the code points in the given charset.
     *
     * @param c a charset
     * @return the set of code points in the charset
     */
    public static CodePoints forCharset(Charset c) {
        if (ENCODABLES.containsKey(c))
            return ENCODABLES.get(c);

        CodePoints points = load(c);
        ENCODABLES.put(c, points);
        return points;
    }

    private static CodePoints load(Charset c) {
        if (!c.canEncode())
            throw new IllegalArgumentException("Charset " + c.name() + " does not support encoding");

        return encodableCodePoints(c.newEncoder());
    }

    void add(CodePointRange range) {
        ranges.add(range);
    }

    private static CodePoints encodableCodePoints(CharsetEncoder encoder) {
        CodePoints points = new CodePoints();

        int start = 0;
        boolean inRange = false;
        int current = 0;
        int previousCount = 0;
        int[] buffer = new int[1];

        for (; current <= Character.MAX_CODE_POINT; ++current) {
            encoder.reset();
            buffer[0] = current;

            String s = new String(buffer, 0, 1);
            if (encoder.canEncode(s)) {
                if (!inRange) {
                    inRange = true;
                    start = current;
                }
            } else if (inRange) {
                inRange = false;
                CodePointRange range = new CodePointRange(start, current - 1, previousCount);
                points.add(range);
                previousCount += range.size();
            }
        }

        if (inRange)
            points.add(new CodePointRange(start, current - 1, previousCount));

        return points;
    }

    static class CodePointRange {
        final int low;
        final int high;
        final int previousCount;

        CodePointRange(int low, int high, int previousCount) {
            if (low > high)
                throw new IllegalArgumentException(format("%d > %d", low, high));

            this.low = low;
            this.high = high;
            this.previousCount = previousCount;
        }

        boolean contains(int codePoint) {
            return codePoint >= low && codePoint <= high;
        }

        int size() {
            return high - low + 1;
        }
    }
}

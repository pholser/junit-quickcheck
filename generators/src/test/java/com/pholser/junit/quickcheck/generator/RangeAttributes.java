/*
 The MIT License

 Copyright (c) 2010-2015 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.generator;

public class RangeAttributes {
    public static byte minByte() {
        return Byte.MIN_VALUE;
    }

    public static byte maxByte() {
        return Byte.MAX_VALUE;
    }

    public static char minChar() {
        return Character.MIN_VALUE;
    }

    public static char maxChar() {
        return Character.MAX_VALUE;
    }

    public static double minDouble() {
        return 0;
    }

    public static double maxDouble() {
        return 1;
    }

    public static float minFloat() {
        return 0F;
    }

    public static float maxFloat() {
        return 1F;
    }

    public static int minInt() {
        return Integer.MIN_VALUE;
    }

    public static int maxInt() {
        return Integer.MAX_VALUE;
    }

    public static long minLong() {
        return Long.MIN_VALUE;
    }

    public static long maxLong() {
        return Long.MAX_VALUE;
    }

    public static short minShort() {
        return Short.MIN_VALUE;
    }

    public static short maxShort() {
        return Short.MAX_VALUE;
    }
}

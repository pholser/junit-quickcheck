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

package com.pholser.junit.quickcheck.generator;

import java.util.Map;

import static com.pholser.junit.quickcheck.Annotations.*;

public final class RangeAttributes {
    private static final Map<String, Object> attributes;

    static {
        attributes = defaultValuesOf(InRange.class);
    }

    private RangeAttributes() {
        throw new UnsupportedOperationException();
    }

    private static <T> T rangeAttribute(String name, Class<T> type) {
        return type.cast(attributes.get(name));
    }

    public static byte minByte() {
        return rangeAttribute("minByte", Byte.class);
    }

    public static byte maxByte() {
        return rangeAttribute("maxByte", Byte.class);
    }

    public static char minChar() {
        return rangeAttribute("minChar", Character.class);
    }

    public static char maxChar() {
        return rangeAttribute("maxChar", Character.class);
    }

    public static double minDouble() {
        return rangeAttribute("minDouble", Double.class);
    }

    public static double maxDouble() {
        return rangeAttribute("maxDouble", Double.class);
    }

    public static float minFloat() {
        return rangeAttribute("minFloat", Float.class);
    }

    public static float maxFloat() {
        return rangeAttribute("maxFloat", Float.class);
    }

    public static int minInt() {
        return rangeAttribute("minInt", Integer.class);
    }

    public static int maxInt() {
        return rangeAttribute("maxInt", Integer.class);
    }

    public static long minLong() {
        return rangeAttribute("minLong", Long.class);
    }

    public static long maxLong() {
        return rangeAttribute("maxLong", Long.class);
    }

    public static short minShort() {
        return rangeAttribute("minShort", Short.class);
    }

    public static short maxShort() {
        return rangeAttribute("maxShort", Short.class);
    }
}

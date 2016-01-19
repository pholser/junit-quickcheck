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

package com.pholser.junit.quickcheck.generator.internal;

import java.util.function.Predicate;

public final class Comparables {
    private Comparables() {
        throw new UnsupportedOperationException();
    }

    public static <T extends Comparable<? super T>> Predicate<T> inRange(T min, T max) {
        return c -> {
            if (min == null && max == null)
                return true;
            if (min == null)
                return c.compareTo(max) <= 0;
            if (max == null)
                return c.compareTo(min) >= 0;
            return c.compareTo(min) >= 0 && c.compareTo(max) <= 0;
        };
    }

    public static <T extends Comparable<? super T>> T leastMagnitude(T min, T max, T zero) {
        if (min == null && max == null)
            return zero;

        if (min == null)
            return max.compareTo(zero) <= 0 ? max : zero;
        if (max == null)
            return min.compareTo(zero) >= 0 ? min : zero;

        if (min.compareTo(zero) > 0)
            return min;
        if (max.compareTo(zero) < 0)
            return max;

        return zero;
    }
}

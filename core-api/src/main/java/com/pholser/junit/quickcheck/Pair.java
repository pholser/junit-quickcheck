/*
 The MIT License

 Copyright (c) 2010-2020 Paul R. Holser, Jr.

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

import com.pholser.junit.quickcheck.generator.Gen;

import java.util.Objects;

/**
 * Typed pair of elements.
 *
 * @param <F> type of first element of pair
 * @param <S> type of second element of pair
 * @see Gen#frequency(Pair, Pair[])
 */
public final class Pair<F, S> {
    public final F first;
    public final S second;

    /**
     * Makes a pair.
     *
     * @param first first element of the pair
     * @param second second element of the pair
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Override public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Pair<?, ?>))
            return false;

        Pair<?, ?> other = (Pair<?, ?>) o;
        return Objects.equals(first, other.first)
            && Objects.equals(second, other.second);
    }

    @Override public String toString() {
        return String.format("[%s = %s]", first, second);
    }
}

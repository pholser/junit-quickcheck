/*
 The MIT License

 Copyright (c) 2010-2018 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.examples.func;

import java.util.Optional;
import java.util.function.Function;

final class Either<L, R> {
    private final Optional<L> left;
    private final Optional<R> right;

    private Either(Optional<L> left, Optional<R> right) {
        this.left = left;
        this.right = right;
    }

    static <A, B> Either<A, B> makeLeft(A left) {
        return new Either<>(Optional.of(left), Optional.empty());
    }

    static <A, B> Either<A, B> makeRight(B right) {
        return new Either<>(Optional.empty(), Optional.of(right));
    }

    <T, U extends T> T map(
        Function<? super L, U> ifLeft,
        Function<? super R, U> ifRight) {

        return left.map(ifLeft)
            .orElseGet(() -> right.map(ifRight).get());
    }

    @Override public boolean equals(Object o) {
        if (!(o instanceof Either<?, ?>))
            return false;

        Either<?, ?> other = (Either<?, ?>) o;
        return left.equals(other.left) && right.equals(other.right);
    }

    @Override public int hashCode() {
        return 17 + 31 * left.hashCode() + 31 * right.hashCode();
    }

    @Override public String toString() {
        return map(
            ell -> "Left[" + ell + ']',
            r -> "Right[" + r + ']');
    }
}

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

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.util.Collections.*;
import static java.util.stream.StreamSupport.*;

import static com.pholser.junit.quickcheck.internal.Sequences.*;

/**
 * Base class for generators of integral types, such as {@code int} and
 * {@link BigInteger}.
 *
 * @param <T> type of values this generator produces
 */
public abstract class IntegralGenerator<T extends Number> extends Generator<T> {
    protected IntegralGenerator(Class<T> type) {
        super(singletonList(type));
    }

    protected IntegralGenerator(List<Class<T>> types) {
        super(types);
    }

    @Override public List<T> doShrink(SourceOfRandomness random, T larger) {
        if (larger.equals(leastMagnitude()))
            return emptyList();

        List<T> results =
            stream(
                halvingIntegral(
                    widen().apply(larger),
                    widen().apply(leastMagnitude())
                ).spliterator(), false)
                .limit(15)
                .map(narrow())
                .filter(inRange())
                .distinct()
                .collect(Collectors.toList());
        Collections.reverse(results);
        results.add(leastMagnitude());
        if (negative(larger))
            results.add(negate(larger));

        return results;
    }

    protected Function<T, BigInteger> widen() {
        return n -> BigInteger.valueOf(n.longValue());
    }

    protected abstract Function<BigInteger, T> narrow();

    protected abstract Predicate<T> inRange();

    protected abstract T leastMagnitude();

    protected abstract boolean negative(T target);

    protected abstract T negate(T target);
}

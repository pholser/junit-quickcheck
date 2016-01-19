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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.util.Collections.*;
import static java.util.stream.StreamSupport.*;

import static com.pholser.junit.quickcheck.internal.Sequences.*;

/**
 * Base class for generators of integral types, such as {@code double} and
 * {@link BigDecimal}.
 *
 * @param <T> type of values this generator produces
 */
public abstract class DecimalGenerator<T extends Number> extends Generator<T> {
    protected DecimalGenerator(Class<T> type) {
        super(singletonList(type));
    }

    protected DecimalGenerator(List<Class<T>> types) {
        super(types);
    }

    @Override public List<T> doShrink(SourceOfRandomness random, T larger) {
        if (larger.equals(leastMagnitude()))
            return emptyList();

        BigDecimal decimal = widen().apply(larger);
        BigDecimal least = widen().apply(leastMagnitude());

        List<T> results = new ArrayList<>();
        results.addAll(shrunkenDecimals(decimal, least));
        results.addAll(shrunkenIntegrals(decimal, least));
        results.add(leastMagnitude());
        if (negative(larger))
            results.add(negate(larger));

        return results;
    }

    private List<T> shrunkenIntegrals(BigDecimal decimal, BigDecimal least) {
        return decimalsFrom(
            stream(halvingIntegral(decimal.toBigInteger(), least.toBigInteger()).spliterator(), false)
                .map(BigDecimal::new));
    }

    private List<T> shrunkenDecimals(BigDecimal decimal, BigDecimal least) {
        return decimalsFrom(stream(halvingDecimal(decimal, least).spliterator(), false));
    }

    private List<T> decimalsFrom(Stream<BigDecimal> stream) {
        List<T> decimals =
            stream.limit(15)
                .map(narrow())
                .filter(inRange())
                .distinct()
                .collect(Collectors.toList());
        Collections.reverse(decimals);

        return decimals;
    }

    protected abstract Function<T, BigDecimal> widen();

    protected abstract Function<BigDecimal, T> narrow();

    protected abstract Predicate<T> inRange();

    protected abstract T leastMagnitude();

    protected abstract boolean negative(T target);

    protected abstract T negate(T target);
}

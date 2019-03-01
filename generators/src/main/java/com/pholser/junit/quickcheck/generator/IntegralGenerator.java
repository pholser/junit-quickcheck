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

package com.pholser.junit.quickcheck.generator;

import static com.pholser.junit.quickcheck.internal.Sequences.halvingIntegral;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Base class for generators of integral types, such as {@code int} and
 * {@link BigInteger}. All numbers are converted to/from BigInteger for processing.
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

        List<T> results =new ArrayList<>();

        // Positive numbers are considered easier than negative ones
        if (negative(larger))
            results.add(negate(larger));

        // Try your luck by testing the smallest possible value
        results.add(leastMagnitude());

        // Try values between smallest and largest, with smaller and smaller increments as we approach the largest
        results.addAll(stream(
                halvingIntegral(
                    // We work with BigInteger, so convert all inputs
                    widen().apply(larger),
                    widen().apply(leastMagnitude())
                ).spliterator(),
                false)
                .limit(15)
                .map(narrow())
                .filter(inRange())
                .distinct()
                .collect(toList()));

        return results;
    }

    /**
     * @return a function converting a value of the base type into a {@link BigInteger}.
     */
    protected Function<T, BigInteger> widen() {
        return n -> BigInteger.valueOf(n.longValue());
    }

    /**
     * @return a function converting a {@link BigInteger} into the equivalent value in the base type.
     */
    protected abstract Function<BigInteger, T> narrow();

    /**
     * @return a predicate checking whether its input is in the configured range.
     */
    protected abstract Predicate<T> inRange();

    /**
     * @return the lowest magnitude number, respecting the configured range. The ideal shrink value is always this value (i.e. this value cannot be shrunk any further).
     */
    protected abstract T leastMagnitude();

    /**
     * @return whether the given number is negative or not.
     */
    protected abstract boolean negative(T target);

    /**
     * Used when shrinking negative numbers to add the positive equivalent value at the top of shrinks list.
     *
     * @param target always a negative number
     * @return the positive equivalent to target
     */
    protected abstract T negate(T target);
}

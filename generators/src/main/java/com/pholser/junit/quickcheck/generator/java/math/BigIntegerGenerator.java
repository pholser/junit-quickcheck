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

package com.pholser.junit.quickcheck.generator.java.math;

import java.math.BigInteger;
import java.util.function.Function;
import java.util.function.Predicate;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.IntegralGenerator;
import com.pholser.junit.quickcheck.generator.internal.Comparables;
import com.pholser.junit.quickcheck.internal.Ranges;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.math.BigInteger.*;
import static java.util.function.Function.*;

import static com.pholser.junit.quickcheck.internal.Ranges.*;
import static com.pholser.junit.quickcheck.internal.Reflection.*;

/**
 * <p>Produces values of type {@link BigInteger}.</p>
 *
 * <p>With no additional configuration, the generated values are chosen from
 * a range with a magnitude decided by
 * {@link com.pholser.junit.quickcheck.generator.GenerationStatus#size()}.</p>
 */
public class BigIntegerGenerator extends IntegralGenerator<BigInteger> {
    private BigInteger min;
    private BigInteger max;

    public BigIntegerGenerator() {
        super(BigInteger.class);
    }

    /**
     * <p>Tells this generator to produce values within a specified
     * {@linkplain InRange#min() minimum} and/or
     * {@linkplain InRange#max() maximum} inclusive, with uniform
     * distribution.</p>
     *
     * <p>If an endpoint of the range is not specified, its value takes on
     * a magnitude influenced by
     * {@link com.pholser.junit.quickcheck.generator.GenerationStatus#size()}.</p>

     * @param range annotation that gives the range's constraints
     */
    public void configure(InRange range) {
        if (!defaultValueOf(InRange.class, "min").equals(range.min()))
            min = new BigInteger(range.min());
        if (!defaultValueOf(InRange.class, "max").equals(range.max()))
            max = new BigInteger(range.max());
        if (min != null && max != null)
            checkRange(Ranges.Type.INTEGRAL, min, max);
    }

    @Override public BigInteger generate(SourceOfRandomness random, GenerationStatus status) {
        int numberOfBits = status.size() + 1;

        if (min == null && max == null)
            return random.nextBigInteger(numberOfBits);

        BigInteger minToUse = min;
        BigInteger maxToUse = max;
        if (minToUse == null)
            minToUse = maxToUse.subtract(TEN.pow(numberOfBits));
        else if (maxToUse == null)
            maxToUse = minToUse.add(TEN.pow(numberOfBits));

        return choose(random, minToUse, maxToUse);
    }

    @Override protected Function<BigInteger, BigInteger> narrow() {
        return identity();
    }

    @Override protected Predicate<BigInteger> inRange() {
        return Comparables.inRange(min, max);
    }

    @Override protected BigInteger leastMagnitude() {
        return Comparables.leastMagnitude(min, max, ZERO);
    }

    @Override protected boolean negative(BigInteger target) {
        return target.signum() < 0;
    }

    @Override protected BigInteger negate(BigInteger target) {
        return target.negate();
    }
}

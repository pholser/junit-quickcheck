/*
 The MIT License

 Copyright (c) 2010-2013 Paul R. Holser, Jr.

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

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static com.pholser.junit.quickcheck.internal.Ranges.*;
import static com.pholser.junit.quickcheck.internal.Reflection.*;
import static java.math.BigInteger.*;

/**
 * <p>Produces values for theory parameters of type {@link java.math.BigInteger}.</p>
 *
 * <p>With no additional configuration, the generated values are chosen from a range with a magnitude decided by
 * {@link com.pholser.junit.quickcheck.generator.GenerationStatus#size()}.</p>
 */
public class BigIntegerGenerator extends Generator<BigInteger> {
    private BigInteger min;
    private BigInteger max;

    public BigIntegerGenerator() {
        super(BigInteger.class);
    }

    /**
     * <p>Tells this generator to produce values within a specified {@linkplain InRange#min() minimum} and/or
     * {@linkplain InRange#max() maximum} inclusive, with uniform distribution.</p>
     *
     * <p>If an endpoint of the range is not specified, its value takes on a magnitude influenced by
     * {@link com.pholser.junit.quickcheck.generator.GenerationStatus#size()}.</p>

     * @param range annotation that gives the range's constraints
     * @throws NumberFormatException if the range's values cannot be converted to {@code BigInteger}
     * @throws IllegalArgumentException if the range's values specify a nonsensical range
     */
    public void configure(InRange range) {
        if (!defaultValueOf(InRange.class, "min").equals(range.min()))
            min = new BigInteger(range.min());
        if (!defaultValueOf(InRange.class, "max").equals(range.max()))
            max = new BigInteger(range.max());
        if (min != null && max != null)
            checkRange("d", min, max);
    }

    @Override
    public BigInteger generate(SourceOfRandomness random, GenerationStatus status) {
        int numberOfBits = status.size() + 1;

        if (min == null && max == null)
            return random.nextBigInteger(numberOfBits);

        BigInteger minToUse = min;
        BigInteger maxToUse = max;
        if (minToUse == null)
            minToUse = maxToUse.subtract(TEN.pow(numberOfBits));
        else if (maxToUse == null)
            maxToUse = minToUse.add(TEN.pow(numberOfBits));

        BigInteger range = maxToUse.subtract(minToUse);
        BigInteger generated;

        do {
            generated = random.nextBigInteger(range.bitLength());
        } while (generated.compareTo(range) >= 0);

        return generated.add(minToUse);
    }
}

/*
 The MIT License

 Copyright (c) 2010-2012 Paul R. Holser, Jr.

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
import java.math.BigInteger;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static com.pholser.junit.quickcheck.internal.Ranges.*;
import static com.pholser.junit.quickcheck.internal.Reflection.*;
import static java.lang.Math.*;
import static java.math.BigDecimal.*;

public class BigDecimalGenerator extends Generator<BigDecimal> {
    private BigDecimal min;
    private BigDecimal max;
    private Precision precision;

    public BigDecimalGenerator() {
        super(BigDecimal.class);
    }

    public void configure(InRange range) {
        if (!defaultValueOf(InRange.class, "min").equals(range.min()))
            min = new BigDecimal(range.min());
        if (!defaultValueOf(InRange.class, "max").equals(range.max()))
            max = new BigDecimal(range.max());
        if (min != null && max != null)
            checkRange("f", min, max);
    }

    public void configure(Precision precision) {
        this.precision = precision;
    }

    @Override
    public BigDecimal generate(SourceOfRandomness random, GenerationStatus status) {
        BigDecimal minToUse = min;
        BigDecimal maxToUse = max;
        int power = status.size() + 1;

        if (minToUse == null && maxToUse == null) {
            maxToUse = TEN.pow(power);
            minToUse = maxToUse.negate();
        }

        int scale = decideScale();

        if (minToUse == null)
            minToUse = maxToUse.subtract(TEN.pow(power));
        else if (maxToUse == null)
            maxToUse = minToUse.add(TEN.pow(power));

        BigDecimal minShifted = minToUse.movePointRight(scale);
        BigDecimal maxShifted = maxToUse.movePointRight(scale);
        BigInteger range = maxShifted.toBigInteger().subtract(minShifted.toBigInteger());

        BigInteger generated;
        do {
            generated = random.nextBigInteger(range.bitLength());
        } while (generated.compareTo(range) >= 0);

        return minShifted.add(new BigDecimal(generated)).movePointLeft(scale);
    }

    private int decideScale() {
        int scale = Integer.MIN_VALUE;

        if (min != null)
            scale = max(scale, min.scale());
        if (max != null)
            scale = max(scale, max.scale());
        if (precision != null)
            scale = max(scale, precision.scale());

        return max(scale, 0);
    }
}

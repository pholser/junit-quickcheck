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

package com.pholser.junit.quickcheck.generator.java.time;

import java.math.BigInteger;
import java.time.Period;
import java.time.Year;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static com.pholser.junit.quickcheck.internal.Ranges.*;
import static com.pholser.junit.quickcheck.internal.Reflection.*;

/**
 * Produces values of type {@link Period}.
 */
public class PeriodGenerator extends Generator<Period> {
    private static final BigInteger TWELVE = BigInteger.valueOf(12);
    private static final BigInteger THIRTY_ONE = BigInteger.valueOf(31);

    private Period min = Period.of(Year.MIN_VALUE, -12, -31);
    private Period max = Period.of(Year.MAX_VALUE, 12, 31);

    public PeriodGenerator() {
        super(Period.class);
    }

    /**
     * <p>Tells this generator to produce values within a specified
     * {@linkplain InRange#min() minimum} and/or {@linkplain InRange#max()
     * maximum}, inclusive, with uniform distribution.</p>
     *
     * <p>If an endpoint of the range is not specified, the generator will use
     * Periods with values of either {@code Period(Year#MIN_VALUE, -12, -31)}
     * or {@code Period(Year#MAX_VALUE, 12, 31)} as appropriate.</p>
     *
     * <p>{@linkplain InRange#format()} is ignored.  Periods are always parsed
     * using formats based on the ISO-8601 period formats {@code PnYnMnD} and
     * {@code PnW}.</p>
     *
     * @see Period#parse(CharSequence)
     * @param range annotation that gives the range's constraints
     */
    public void configure(InRange range) {
        if (!defaultValueOf(InRange.class, "min").equals(range.min()))
            min = Period.parse(range.min());
        if (!defaultValueOf(InRange.class, "max").equals(range.max()))
            max = Period.parse(range.max());

        if (toBigInteger(min).compareTo(toBigInteger(max)) > 0)
            throw new IllegalArgumentException(String.format("bad range, %s > %s", range.min(), range.max()));
    }

    @Override public Period generate(SourceOfRandomness random, GenerationStatus status) {
        return fromBigInteger(choose(random, toBigInteger(min), toBigInteger(max)));
    }

    private BigInteger toBigInteger(Period period) {
        return BigInteger.valueOf(period.getYears())
            .multiply(TWELVE)
            .add(BigInteger.valueOf(period.getMonths()))
            .multiply(THIRTY_ONE)
            .add(BigInteger.valueOf(period.getDays()));
    }

    private Period fromBigInteger(BigInteger period) {
        BigInteger[] monthsAndDays = period.divideAndRemainder(THIRTY_ONE);
        BigInteger[] yearsAndMonths = monthsAndDays[0].divideAndRemainder(TWELVE);

        return Period.of(
            yearsAndMonths[0].intValueExact(),
            yearsAndMonths[1].intValueExact(),
            monthsAndDays[1].intValueExact());
    }
}

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

package com.pholser.junit.quickcheck.generator.java.math;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static java.math.BigInteger.*;
import static java.util.Arrays.*;

import com.google.common.collect.ImmutableMap;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Precision;
import com.pholser.junit.quickcheck.internal.generator.GeneratingUniformRandomValuesForTheoryParameterTest;

import static org.mockito.Mockito.*;

public class RangedBigDecimalWithLesserSpecifiedPrecisionTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    private final BigDecimal min = new BigDecimal("-12345678123456781234567812345.678");
    private final BigDecimal max = new BigDecimal("9876543219876543.21");
    private int numberOfBits;

    @Override protected void primeSourceOfRandomness() {
        numberOfBits = max.movePointRight(3).subtract(min.movePointRight(3)).toBigInteger().bitLength();
        when(randomForParameterGenerator.nextBigInteger(numberOfBits))
            .thenReturn(new BigInteger("2").pow(numberOfBits).subtract(ONE)).thenReturn(ONE).thenReturn(TEN)
            .thenReturn(ZERO).thenReturn(new BigInteger("234234234234"));
    }

    @Override protected Type parameterType() {
        return BigDecimal.class;
    }

    @Override protected int sampleSize() {
        return 4;
    }

    @Override protected List<?> randomValues() {
        return asList(new BigDecimal("-12345678123456781234567812345.677"),
            new BigDecimal("-12345678123456781234567812345.668"),
            min,
            new BigDecimal("-12345678123456781234333578111.444"));
    }

    @Override protected Map<Class<? extends Annotation>, Annotation> configurations() {
        InRange range = mock(InRange.class);
        when(range.min()).thenReturn(min.toString());
        when(range.max()).thenReturn(max.toString());
        Precision precision = mock(Precision.class);
        when(precision.scale()).thenReturn(2);
        return ImmutableMap.<Class<? extends Annotation>, Annotation> builder()
            .put(InRange.class, range)
            .put(Precision.class, precision)
            .build();
    }

    @Override public void verifyInteractionWithRandomness() {
        verify(randomForParameterGenerator, times(5)).nextBigInteger(numberOfBits);
    }
}

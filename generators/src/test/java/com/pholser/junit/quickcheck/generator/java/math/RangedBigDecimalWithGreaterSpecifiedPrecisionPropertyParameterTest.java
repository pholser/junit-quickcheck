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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.pholser.junit.quickcheck.generator.BasicGeneratorPropertyParameterTest;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Precision;

import static java.math.BigInteger.*;
import static java.util.Arrays.*;
import static org.mockito.Mockito.*;

public class RangedBigDecimalWithGreaterSpecifiedPrecisionPropertyParameterTest
    extends BasicGeneratorPropertyParameterTest {

    @InRange(min = "-12345678123456781234567812345.678", max = "9876543219876543.21")
    @Precision(scale = 6)
    public static final BigDecimal TYPE_BEARER = null;

    private final BigDecimal min = new BigDecimal("-12345678123456781234567812345.678");
    private final BigDecimal max = new BigDecimal("9876543219876543.21");
    private int numberOfBits;

    @Override protected void primeSourceOfRandomness() {
        numberOfBits = max.movePointRight(6).subtract(min.movePointRight(6)).toBigInteger().bitLength();
        when(randomForParameterGenerator.nextBigInteger(numberOfBits))
            .thenReturn(new BigInteger("2").pow(numberOfBits).subtract(ONE))
            .thenReturn(ONE)
            .thenReturn(TEN)
            .thenReturn(ZERO)
            .thenReturn(new BigInteger("234234234234"));
    }

    @Override protected int trials() {
        return 4;
    }

    @Override protected List<?> randomValues() {
        return asList(
            new BigDecimal("-12345678123456781234567812345.677999"),
            new BigDecimal("-12345678123456781234567812345.677990"),
            min.setScale(6),
            new BigDecimal("-12345678123456781234567578111.443766"));
    }

    @Override public void verifyInteractionWithRandomness() {
        verify(randomForParameterGenerator, times(5)).nextBigInteger(numberOfBits);
    }
}

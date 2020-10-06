/*
 The MIT License

 Copyright (c) 2010-2020 Paul R. Holser, Jr.

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

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TEN;
import static java.math.BigInteger.ZERO;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pholser.junit.quickcheck.generator.BasicGeneratorPropertyParameterTest;
import com.pholser.junit.quickcheck.generator.InRange;
import java.math.BigInteger;
import java.util.List;

public class RangedBigIntegerPropertyParameterTest
    extends BasicGeneratorPropertyParameterTest {

    @InRange(min = "-12345678123456781234567812345678", max = "987654321987654321")
    public static final BigInteger TYPE_BEARER = null;

    private final BigInteger min = new BigInteger("-12345678123456781234567812345678");
    private final BigInteger max = new BigInteger("987654321987654321");

    @Override protected void primeSourceOfRandomness() {
        int numberOfBits = max.subtract(min).bitLength();
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
        return asList(min.add(ONE), min.add(TEN), min.add(ZERO), min.add(new BigInteger("234234234234")));
    }

    @Override public void verifyInteractionWithRandomness() {
        verify(randomForParameterGenerator, times(5)).nextBigInteger(max.subtract(min).bitLength());
    }
}

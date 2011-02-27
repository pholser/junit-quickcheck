package com.pholser.junit.parameters.internal;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static java.util.Arrays.*;

import static org.mockito.Mockito.*;

public class GeneratingUniformRandomValuesForBigDecimalTheoryParametersTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextInt(0, 100)).thenReturn(1);
        when(random.nextBytes(1)).thenReturn(bytesOf("a")).thenReturn(bytesOf("b")).thenReturn(bytesOf("c"));
        when(random.nextInt()).thenReturn(1).thenReturn(2).thenReturn(3);
    }

    @Override
    protected Class<?> parameterType() {
        return BigDecimal.class;
    }

    @Override
    protected int sampleSize() {
        return 3;
    }

    @Override
    protected List<?> randomValues() {
        return asList(new BigDecimal(BigInteger.valueOf('a'), 1),
            new BigDecimal(BigInteger.valueOf('b'), 2),
            new BigDecimal(BigInteger.valueOf('c'), 3));
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random, times(3)).nextInt(0, 100);
        verify(random, times(3)).nextBytes(1);
    }

    private static byte[] bytesOf(String s) {
        try {
            return s.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException shouldNotHappen) {
            throw new IllegalStateException(shouldNotHappen);
        }
    }
}

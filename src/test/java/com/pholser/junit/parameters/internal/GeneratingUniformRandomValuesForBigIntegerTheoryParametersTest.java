package com.pholser.junit.parameters.internal;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.List;

import static java.util.Arrays.*;

import static org.mockito.Mockito.*;

public class GeneratingUniformRandomValuesForBigIntegerTheoryParametersTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextInt(0, 100)).thenReturn(2).thenReturn(1);
        when(random.nextBytes(2)).thenReturn(bytesOf("ab"));
        when(random.nextBytes(1)).thenReturn(bytesOf("c"));
    }

    @Override
    protected Class<?> parameterType() {
        return BigInteger.class;
    }

    @Override
    protected int sampleSize() {
        return 2;
    }

    @Override
    protected List<?> randomValues() {
        return asList(new BigInteger(bytesOf("ab")), new BigInteger(bytesOf("c")));
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random, times(2)).nextInt(0, 100);
        verify(random).nextBytes(2);
        verify(random).nextBytes(1);
    }

    private static byte[] bytesOf(String s) {
        try {
            return s.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException shouldNotHappen) {
            throw new IllegalStateException(shouldNotHappen);
        }
    }
}

package com.pholser.junit.parameters.internal;

import java.util.List;

import static java.lang.Byte.*;
import static java.util.Arrays.*;

import static org.mockito.Mockito.*;

public class GeneratingUniformRandomValuesForWrapperByteTheoryParametersTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextInt(MIN_VALUE, MAX_VALUE)).thenReturn(-95).thenReturn(-94).thenReturn(-93)
            .thenReturn(-92);
    }

    @Override
    protected Class<?> parameterType() {
        return Byte.class;
    }

    @Override
    protected int sampleSize() {
        return 4;
    }

    @Override
    protected List<?> randomValues() {
        byte b = (byte) 0xA1;
        return asList(b++, b++, b++, b);
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random, times(4)).nextInt(MIN_VALUE, MAX_VALUE);
    }
}

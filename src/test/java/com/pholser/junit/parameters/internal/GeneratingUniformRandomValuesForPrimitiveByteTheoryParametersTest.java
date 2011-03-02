package com.pholser.junit.parameters.internal;

import java.lang.reflect.Type;
import java.util.List;

import static java.lang.Byte.*;
import static java.util.Arrays.*;

import static org.mockito.Mockito.*;

public class GeneratingUniformRandomValuesForPrimitiveByteTheoryParametersTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextInt(MIN_VALUE, MAX_VALUE)).thenReturn(-12).thenReturn(-11).thenReturn(-10);
    }

    @Override
    protected Type parameterType() {
        return byte.class;
    }

    @Override
    protected int sampleSize() {
        return 3;
    }

    @Override
    protected List<?> randomValues() {
        byte b = (byte) 0xF4;
        return asList(b++, b++, b);
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random, times(3)).nextInt(MIN_VALUE, MAX_VALUE);
    }
}

package com.pholser.junit.parameters.internal;

import java.util.List;

import static java.util.Arrays.*;

import static org.mockito.Mockito.*;

public class GeneratingUniformRandomValuesForPrimitiveBooleanTheoryParametersTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextBoolean()).thenReturn(false).thenReturn(true).thenReturn(true).thenReturn(false);
    }

    @Override
    protected Class<?> parameterType() {
        return boolean.class;
    }

    @Override
    protected int sampleSize() {
        return 4;
    }

    @Override
    protected List<?> randomValues() {
        return asList(false, true, true, false);
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random, times(4)).nextBoolean();
    }
}

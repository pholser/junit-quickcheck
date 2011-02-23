package com.pholser.junit.parameters.internal;

import java.util.List;

import static java.util.Arrays.*;

import static org.mockito.Mockito.*;

public class GeneratingUniformRandomValuesForWrapperBooleanTheoryParametersTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextBoolean()).thenReturn(true).thenReturn(false);
    }

    @Override
    protected Class<?> parameterType() {
        return Boolean.class;
    }

    @Override
    protected int sampleSize() {
        return 2;
    }

    @Override
    protected List<?> randomValues() {
        return asList(true, false);
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random, times(2)).nextBoolean();
    }
}

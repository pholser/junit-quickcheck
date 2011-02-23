package com.pholser.junit.parameters.internal;

import java.util.List;

import static java.util.Arrays.*;

import static org.mockito.Mockito.*;

public class GeneratingUniformRandomValuesForWrapperLongTheoryParametersTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextLong()).thenReturn(-3L).thenReturn(-2L);
    }

    @Override
    protected Class<?> parameterType() {
        return Long.class;
    }

    @Override
    protected int sampleSize() {
        return 2;
    }

    @Override
    protected List<?> randomValues() {
        return asList(-3L, -2L);
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random, times(2)).nextLong();
    }
}

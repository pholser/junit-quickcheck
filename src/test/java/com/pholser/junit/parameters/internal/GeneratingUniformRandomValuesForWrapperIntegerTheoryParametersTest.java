package com.pholser.junit.parameters.internal;

import java.util.List;

import static java.util.Arrays.*;

import static org.mockito.Mockito.*;

public class GeneratingUniformRandomValuesForWrapperIntegerTheoryParametersTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextInt()).thenReturn(-3).thenReturn(-4).thenReturn(-5);
    }

    @Override
    protected Class<?> parameterType() {
        return Integer.class;
    }

    @Override
    protected int sampleSize() {
        return 3;
    }

    @Override
    protected List<?> randomValues() {
        return asList(-3, -4, -5);
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random, times(3)).nextInt();
    }
}

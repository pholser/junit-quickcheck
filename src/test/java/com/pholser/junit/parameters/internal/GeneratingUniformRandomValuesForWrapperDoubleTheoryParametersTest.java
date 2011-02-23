package com.pholser.junit.parameters.internal;

import java.util.List;

import static java.util.Arrays.*;

import static org.mockito.Mockito.*;

public class GeneratingUniformRandomValuesForWrapperDoubleTheoryParametersTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextDouble()).thenReturn(7D).thenReturn(8D).thenReturn(9D).thenReturn(2D);
    }

    @Override
    protected Class<?> parameterType() {
        return Double.class;
    }

    @Override
    protected int sampleSize() {
        return 4;
    }

    @Override
    protected List<?> randomValues() {
        return asList(7D, 8D, 9D, 2D);
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random, times(4)).nextDouble();
    }
}

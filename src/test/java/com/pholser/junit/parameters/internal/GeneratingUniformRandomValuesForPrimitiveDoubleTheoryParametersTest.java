package com.pholser.junit.parameters.internal;

import java.util.List;

import static java.util.Arrays.*;

import static org.mockito.Mockito.*;

public class GeneratingUniformRandomValuesForPrimitiveDoubleTheoryParametersTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextDouble()).thenReturn(2D).thenReturn(3D).thenReturn(4D);
    }

    @Override
    protected Class<?> parameterType() {
        return double.class;
    }

    @Override
    protected int sampleSize() {
        return 3;
    }

    @Override
    protected List<?> randomValues() {
        return asList(2D, 3D, 4D);
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random, times(3)).nextDouble();
    }
}

package com.pholser.junit.parameters.internal;

import java.util.List;

import static java.util.Arrays.*;

import static org.mockito.Mockito.*;

public class GeneratingUniformRandomValuesForPrimitiveIntegerTheoryParametersTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextInt()).thenReturn(-1).thenReturn(-2);
    }

    @Override
    protected Class<?> parameterType() {
        return int.class;
    }

    @Override
    protected int sampleSize() {
        return 2;
    }

    @Override
    protected List<?> randomValues() {
        return asList(-1, -2);
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random, times(2)).nextInt();
    }
}

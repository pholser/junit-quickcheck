package com.pholser.junit.parameters.internal;

import java.util.List;

import static java.lang.Short.*;
import static java.util.Arrays.*;

import static org.mockito.Mockito.*;

public class GeneratingUniformRandomValuesForWrapperShortTheoryParametersTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextInt(MIN_VALUE, MAX_VALUE)).thenReturn(-9).thenReturn(-8).thenReturn(-7)
            .thenReturn(-6).thenReturn(-5);
    }

    @Override
    protected Class<?> parameterType() {
        return Short.class;
    }

    @Override
    protected int sampleSize() {
        return 5;
    }

    @Override
    protected List<?> randomValues() {
        short sh = -9;
        return asList(sh++, sh++, sh++, sh++, sh);
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random, times(5)).nextInt(MIN_VALUE, MAX_VALUE);
    }
}

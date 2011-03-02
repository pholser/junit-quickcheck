package com.pholser.junit.parameters.internal;

import java.lang.reflect.Type;
import java.util.List;

import static java.util.Arrays.*;

import static org.mockito.Mockito.*;

public class GeneratingUniformRandomValuesForWrapperFloatTheoryParametersTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextFloat()).thenReturn(-6F).thenReturn(-5F).thenReturn(-4F);
    }

    @Override
    protected Type parameterType() {
        return Float.class;
    }

    @Override
    protected int sampleSize() {
        return 3;
    }

    @Override
    protected List<?> randomValues() {
        return asList(-6F, -5F, -4F);
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random, times(3)).nextFloat();
    }
}

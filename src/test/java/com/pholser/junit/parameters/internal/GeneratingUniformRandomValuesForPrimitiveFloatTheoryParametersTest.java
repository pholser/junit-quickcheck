package com.pholser.junit.parameters.internal;

import java.lang.reflect.Type;
import java.util.List;

import static java.util.Arrays.*;

import static org.mockito.Mockito.*;

public class GeneratingUniformRandomValuesForPrimitiveFloatTheoryParametersTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextFloat()).thenReturn(7F).thenReturn(8F);
    }

    @Override
    protected Type parameterType() {
        return float.class;
    }

    @Override
    protected int sampleSize() {
        return 2;
    }

    @Override
    protected List<?> randomValues() {
        return asList(7F, 8F);
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random, times(2)).nextFloat();
    }
}

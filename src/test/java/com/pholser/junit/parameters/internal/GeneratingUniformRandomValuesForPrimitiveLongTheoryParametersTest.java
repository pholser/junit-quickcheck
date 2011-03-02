package com.pholser.junit.parameters.internal;

import java.lang.reflect.Type;
import java.util.List;

import static java.lang.Long.*;
import static java.util.Arrays.*;

import static org.mockito.Mockito.*;

public class GeneratingUniformRandomValuesForPrimitiveLongTheoryParametersTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextLong()).thenReturn(MAX_VALUE);
    }

    @Override
    protected Type parameterType() {
        return long.class;
    }

    @Override
    protected int sampleSize() {
        return 1;
    }

    @Override
    protected List<?> randomValues() {
        return asList(MAX_VALUE);
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random).nextLong();
    }
}

package com.pholser.junit.parameters.internal;

import java.lang.reflect.Type;
import java.util.List;

import static java.lang.Character.*;
import static java.util.Arrays.*;

import static org.mockito.Mockito.*;

public class GeneratingUniformRandomValuesForPrimitiveCharTheoryParametersTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextInt(MIN_VALUE, MAX_VALUE)).thenReturn(Integer.valueOf('t'));
    }

    @Override
    protected Type parameterType() {
        return char.class;
    }

    @Override
    protected int sampleSize() {
        return 1;
    }

    @Override
    protected List<?> randomValues() {
        return asList('t');
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random).nextInt(MIN_VALUE, MAX_VALUE);
    }
}

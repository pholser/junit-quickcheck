package com.pholser.junit.parameters.internal;

import java.lang.reflect.Type;
import java.util.List;

import static java.lang.Character.*;
import static java.util.Arrays.*;

import static org.mockito.Mockito.*;

public class GeneratingUniformRandomValuesForWrapperCharacterTheoryParametersTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextInt(MIN_VALUE, MAX_VALUE)).thenReturn(Integer.valueOf('Y'))
            .thenReturn(Integer.valueOf('Z'));
    }

    @Override
    protected Type parameterType() {
        return Character.class;
    }

    @Override
    protected int sampleSize() {
        return 2;
    }

    @Override
    protected List<?> randomValues() {
        return asList('Y', 'Z');
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random, times(2)).nextInt(MIN_VALUE, MAX_VALUE);
    }
}

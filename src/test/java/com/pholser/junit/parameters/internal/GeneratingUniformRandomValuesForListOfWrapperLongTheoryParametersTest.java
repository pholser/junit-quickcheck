package com.pholser.junit.parameters.internal;

import java.lang.reflect.Type;
import java.util.List;

import static java.util.Arrays.*;

import com.pholser.junit.parameters.reflect.ParameterizedTypeImpl;

import static org.mockito.Mockito.*;

public class GeneratingUniformRandomValuesForListOfWrapperLongTheoryParametersTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextInt(0, 100)).thenReturn(2);
        when(random.nextLong()).thenReturn(-3L).thenReturn(-2L);
    }

    @Override
    protected Type parameterType() {
        return new ParameterizedTypeImpl(List.class, Long.class);
    }

    @Override
    protected int sampleSize() {
        return 1;
    }

    @Override
    protected List<?> randomValues() {
        return asList(asList(-3L, -2L));
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random).nextInt(0, 100);
        verify(random, times(2)).nextLong();
    }
}

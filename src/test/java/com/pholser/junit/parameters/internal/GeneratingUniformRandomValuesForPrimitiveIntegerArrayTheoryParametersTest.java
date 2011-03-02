package com.pholser.junit.parameters.internal;

import java.lang.reflect.Type;
import java.util.List;

import static java.util.Arrays.*;

import static org.mockito.Mockito.*;

public class GeneratingUniformRandomValuesForPrimitiveIntegerArrayTheoryParametersTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextInt(0, 100)).thenReturn(1).thenReturn(4);
        when(random.nextInt()).thenReturn(-1).thenReturn(-2).thenReturn(-3).thenReturn(-4).thenReturn(-5);
    }

    @Override
    protected Type parameterType() {
        return int[].class;
    }

    @Override
    protected int sampleSize() {
        return 2;
    }

    @Override
    protected List<?> randomValues() {
        return asList(new int[] {-1}, new int[] {-2, -3, -4, -5});
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random, times(2)).nextInt(0, 100);
        verify(random, times(5)).nextInt();
    }
}

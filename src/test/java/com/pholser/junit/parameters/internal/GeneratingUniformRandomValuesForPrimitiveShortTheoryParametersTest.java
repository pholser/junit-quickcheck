package com.pholser.junit.parameters.internal;

import java.lang.reflect.Type;
import java.util.List;

import static java.lang.Short.*;
import static java.util.Arrays.*;

import static org.mockito.Mockito.*;

public class GeneratingUniformRandomValuesForPrimitiveShortTheoryParametersTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextInt(MIN_VALUE, MAX_VALUE)).thenReturn(0x0004).thenReturn(0x0005)
            .thenReturn(0x0006).thenReturn(0x0007);
    }

    @Override
    protected Type parameterType() {
        return short.class;
    }

    @Override
    protected int sampleSize() {
        return 4;
    }

    @Override
    protected List<?> randomValues() {
        short sh = 0x0004;
        return asList(sh++, sh++, sh++, sh);
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random, times(4)).nextInt(MIN_VALUE, MAX_VALUE);
    }
}

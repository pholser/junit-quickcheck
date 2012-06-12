package com.pholser.junit.quickcheck.generator;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import com.pholser.junit.quickcheck.internal.generator.GeneratingUniformRandomValuesForTheoryParameterTest;

public class DateTest extends GeneratingUniformRandomValuesForTheoryParameterTest {
    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextLong())
            .thenReturn(0L).thenReturn(60000L).thenReturn(100000000L).thenReturn(300000000000L);
    }

    @Override
    protected Type parameterType() {
        return Date.class;
    }

    @Override
    protected int sampleSize() {
        return 4;
    }

    @Override
    protected List<?> randomValues() {
        return asList(new Date(0), new Date(60000), new Date(100000000), new Date(300000000000L));
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random, times(4)).nextLong();
    }
}

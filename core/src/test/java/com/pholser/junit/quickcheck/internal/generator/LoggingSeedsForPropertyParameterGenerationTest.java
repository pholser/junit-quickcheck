package com.pholser.junit.quickcheck.internal.generator;

import java.util.List;

import org.junit.Test;

import static java.util.Collections.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.*;

public class LoggingSeedsForPropertyParameterGenerationTest
    extends CorePropertyParameterTest {

    public static final int TYPE_BEARER = 1;

    @Override protected void primeSourceOfRandomness() {
        when(randomForParameterGenerator.nextInt()).thenReturn(-2);
    }

    @Override protected long seed() {
        return -4L;
    }

    @Override protected int trials() {
        return 1;
    }

    @Override protected List<?> randomValues() {
        return singletonList(-2);
    }

    @Override public void verifyInteractionWithRandomness() {
        verify(randomForParameterGenerator).nextInt();
    }

    @Test public void logFormat() {
        verify(log).debug(
            eq("Seed for parameter {} is {}"),
            matches(".*AnnotatedTypeBaseImpl.*:arg"),
            eq(-4L));
    }
}

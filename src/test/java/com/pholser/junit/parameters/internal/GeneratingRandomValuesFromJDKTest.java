package com.pholser.junit.parameters.internal;

import java.util.Random;

import org.junit.Test;

import static org.mockito.Mockito.*;

public class GeneratingRandomValuesFromJDKTest {
    @Test
    public void fetchesNextIntFromAJavaUtilRandom() {
        Random random = mock(Random.class);
        JDKSourceOfRandomness source = new JDKSourceOfRandomness(random);

        source.nextInt();

        verify(random).nextInt();
    }
}

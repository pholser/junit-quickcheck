package com.pholser.junit.parameters.random;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class AbstractJDKSourceOfRandomnessWrapsAJDKRandomTest {
    private Random jdkRandom;
    private AbstractJDKSourceOfRandomness source;

    @Before
    public void setUp() {
        jdkRandom = mock(Random.class);
        source = new AbstractJDKSourceOfRandomness(jdkRandom) {
            // empty on purpose
        };
    }

    @Test
    public void randomBoolean() {
        source.nextBoolean();

        verify(jdkRandom).nextBoolean();
    }

    @Test
    public void randomBytes() {
        byte[] bytes = new byte[0];

        source.nextBytes(bytes);

        verify(jdkRandom).nextBytes(same(bytes));
    }

    @Test
    public void randomDouble() {
        source.nextDouble();

        verify(jdkRandom).nextDouble();
    }

    @Test
    public void randomFloat() {
        source.nextFloat();

        verify(jdkRandom).nextFloat();
    }

    @Test
    public void randomDoubleGaussianDistro() {
        source.nextGaussian();

        verify(jdkRandom).nextGaussian();
    }

    @Test
    public void randomInteger() {
        source.nextInt();

        verify(jdkRandom).nextInt();
    }

    @Test
    public void randomIntegerInRange() {
        source.nextInt(2);

        verify(jdkRandom).nextInt(eq(2));
    }

    @Test
    public void randomLong() {
        source.nextLong();

        verify(jdkRandom).nextLong();
    }

    @Test
    public void settingSeedForRandomness() {
        source.setSeed(-1L);

        verify(jdkRandom).setSeed(eq(-1L));
    }
}

package com.pholser.junit.parameters.internal;

import java.security.SecureRandom;
import java.util.Random;

import static java.lang.Double.*;
import static java.lang.Float.*;
import static java.lang.String.*;

public class JDKSourceOfRandomness implements SourceOfRandomness {
    private final Random random;

    public JDKSourceOfRandomness() {
        this(new SecureRandom());
    }

    protected JDKSourceOfRandomness(Random random) {
        this.random = random;
    }

    @Override
    public int nextInt() {
        return random.nextInt();
    }

    @Override
    public int nextInt(int min, int max) {
        if (min >= max)
            throw new IllegalArgumentException(format("bad range, {0} >= {1}", min, max));
        return random.nextInt(max - min + 1) + min;
    }

    @Override
    public boolean nextBoolean() {
        return random.nextBoolean();
    }

    @Override
    public long nextLong() {
        return random.nextLong();
    }

    @Override
    public float nextFloat() {
        float result;

        do {
            result = intBitsToFloat(nextInt());
        } while (Float.isNaN(result) || Float.isInfinite(result));

        return result;
    }

    @Override
    public double nextDouble() {
        double result;

        do {
            long ell = nextLong();
            result = longBitsToDouble(ell);
        } while (Double.isNaN(result) || Double.isInfinite(result));

        return result;
    }
}

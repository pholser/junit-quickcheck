package com.pholser.junit.parameters.internal;

import java.security.SecureRandom;
import java.util.Random;

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
}

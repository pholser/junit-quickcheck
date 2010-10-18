package com.pholser.junit.parameters;

import java.security.SecureRandom;

public class SecureJDKSourceOfRandomness extends AbstractJDKSourceOfRandomness {
    public SecureJDKSourceOfRandomness() {
        super(new SecureRandom());
    }

    public SecureJDKSourceOfRandomness(byte[] seed) {
        super(new SecureRandom(seed));
    }
}

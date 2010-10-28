package com.pholser.junit.parameters.random;

import java.util.Random;

public class JDKSourceOfRandomness extends AbstractJDKSourceOfRandomness {
    public JDKSourceOfRandomness() {
        super(new Random());
    }
}

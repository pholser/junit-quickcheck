package com.pholser.junit.parameters;

import java.util.Random;

public class JDKSourceOfRandomness extends AbstractJDKSourceOfRandomness {
    public JDKSourceOfRandomness() {
        super(new Random());
    }
}

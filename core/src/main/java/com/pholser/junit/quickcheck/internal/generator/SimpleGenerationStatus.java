package com.pholser.junit.quickcheck.internal.generator;

import com.pholser.junit.quickcheck.internal.GeometricDistribution;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class SimpleGenerationStatus extends AbstractGenerationStatus {

    private final int attempts;

    public SimpleGenerationStatus(GeometricDistribution distro, SourceOfRandomness random, int attempts) {
        super(distro, random);
        this.attempts = attempts;
    }

    @Override public int attempts() {
        return attempts;
    }

}

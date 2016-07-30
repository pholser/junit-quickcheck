package com.pholser.junit.quickcheck.internal.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.internal.GeometricDistribution;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public abstract class AbstractGenerationStatus implements GenerationStatus {

    protected final GeometricDistribution distro;
    protected final SourceOfRandomness random;

    protected AbstractGenerationStatus(GeometricDistribution distro, SourceOfRandomness random) {
        this.distro = distro;
        this.random = random;
    }

    @Override public int size() {
        return distro.sampleWithMean(attempts() + 1, random);
    }

}

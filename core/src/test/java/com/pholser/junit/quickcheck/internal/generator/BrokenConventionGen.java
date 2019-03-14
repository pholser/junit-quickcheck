package com.pholser.junit.quickcheck.internal.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class BrokenConventionGen extends Generator<Void> {

    public BrokenConventionGen() {
        super(Void.class);
    }

    @Override
    public Void generate(SourceOfRandomness random, GenerationStatus status) {
        return null;
    }
}

package com.pholser.junit.quickcheck.internal.generator.conventiontestclasses;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class GeneratesOtherTypesGen extends Generator<Void> {

    public GeneratesOtherTypesGen() {
        super(Void.class);
    }

    @Override
    public Void generate(SourceOfRandomness random, GenerationStatus status) {
        return null;
    }
}

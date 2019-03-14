package com.pholser.junit.quickcheck.examples.convention;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class ConventionGen extends Generator<Convention> {


    public ConventionGen() {
        super(Convention.class);
    }

    @Override
    public Convention generate(SourceOfRandomness random, GenerationStatus status) {
        return new Convention();
    }
}

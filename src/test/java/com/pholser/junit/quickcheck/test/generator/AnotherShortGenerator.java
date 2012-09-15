package com.pholser.junit.quickcheck.test.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;

public class AnotherShortGenerator extends Generator<Short> {
    public AnotherShortGenerator() {
        super(short.class);
    }

    @Override
    public Short generate(SourceOfRandomness random, GenerationStatus status) {
        return (short) status.size();
    }
}

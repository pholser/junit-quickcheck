package com.pholser.junit.quickcheck.internal.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.Zilch;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class ZilchGenerator extends Generator<Zilch> {
    public ZilchGenerator() {
        super(Zilch.class);
    }

    @Override public Zilch generate(SourceOfRandomness random, GenerationStatus status) {
        return Zilch.INSTANCE;
    }
}

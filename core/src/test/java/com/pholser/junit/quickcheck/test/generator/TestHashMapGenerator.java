package com.pholser.junit.quickcheck.test.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.util.ArrayList;
import java.util.HashMap;

public class TestHashMapGenerator extends Generator<HashMap> {
    public TestHashMapGenerator() {
        super(HashMap.class);
    }

    @Override public HashMap<?, ?> generate(SourceOfRandomness random, GenerationStatus status) {
        return new HashMap<Object, Object>(status.size());
    }
}

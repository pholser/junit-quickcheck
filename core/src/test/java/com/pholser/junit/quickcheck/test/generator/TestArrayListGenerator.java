package com.pholser.junit.quickcheck.test.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.util.ArrayList;

public class TestArrayListGenerator extends Generator<ArrayList> {
    public TestArrayListGenerator() {
        super(ArrayList.class);
    }

    @Override public ArrayList<?> generate(SourceOfRandomness random, GenerationStatus status) {
        return new ArrayList<Object>(status.size());
    }
}

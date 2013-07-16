package com.pholser.junit.quickcheck.test.generator;

import com.pholser.junit.quickcheck.generator.ComponentizedGenerator;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class BoxGenerator extends ComponentizedGenerator<Box> {
    public BoxGenerator() {
        super(Box.class);
    }

    @Override public Box<?> generate(SourceOfRandomness random, GenerationStatus status) {
        return new Box<Object>(componentGenerators().get(0).generate(random, status));
    }

    @Override
    public int numberOfNeededComponents() {
        return 1;
    }
}

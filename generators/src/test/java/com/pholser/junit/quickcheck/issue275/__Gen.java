package com.pholser.junit.quickcheck.issue275;

import com.pholser.junit.quickcheck.generator.ComponentizedGenerator;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class __Gen extends ComponentizedGenerator<__> {
    public __Gen() {
        super(__.class);
    }

    @Override
    public int numberOfNeededComponents() {
        return 2;
    }

    @Override
    public __<?, ?> generate(SourceOfRandomness r, GenerationStatus status) {
        return null;
    }
}

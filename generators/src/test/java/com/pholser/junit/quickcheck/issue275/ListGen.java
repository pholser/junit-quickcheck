package com.pholser.junit.quickcheck.issue275;

import com.pholser.junit.quickcheck.generator.ComponentizedGenerator;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class ListGen extends ComponentizedGenerator<List> {
    public ListGen() {
        super(List.class);
    }

    @Override public int numberOfNeededComponents() {
        return 1;
    }

    @Override public List<?> generate(
        SourceOfRandomness random,
        GenerationStatus status) {

        return new List<Object>() {};
    }
}

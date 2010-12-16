package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.Between;

import com.pholser.junit.parameters.extractors.RandomValueExtractor;
import com.pholser.junit.parameters.random.SourceOfRandomness;

public class BooleanExtractor implements RandomValueExtractor<Boolean> {
    @Override
    public Boolean randomValue(SourceOfRandomness random) {
        return random.nextBoolean();
    }

    @Override
    public boolean supportsRange() {
        return false;
    }

    @Override
    public void applyRange(Between range) {
        throw new UnsupportedOperationException();
    }
}

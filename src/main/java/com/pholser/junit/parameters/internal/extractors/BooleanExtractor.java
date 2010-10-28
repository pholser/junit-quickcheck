package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.extractors.RandomValueExtractor;
import com.pholser.junit.parameters.random.SourceOfRandomness;

public class BooleanExtractor implements RandomValueExtractor<Boolean> {
    @Override
    public Boolean randomValue(SourceOfRandomness random) {
        return random.nextBoolean();
    }
}

package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.extractors.RandomValueExtractor;
import com.pholser.junit.parameters.random.SourceOfRandomness;

public class LongExtractor implements RandomValueExtractor<Long> {
    @Override
    public Long randomValue(SourceOfRandomness random) {
        return random.nextLong();
    }
}

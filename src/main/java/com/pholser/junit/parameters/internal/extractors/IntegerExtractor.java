package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.extractors.RandomValueExtractor;
import com.pholser.junit.parameters.random.SourceOfRandomness;

public class IntegerExtractor implements RandomValueExtractor<Integer> {
    @Override
    public Integer randomValue(SourceOfRandomness random) {
        return random.nextInt();
    }
}

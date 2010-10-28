package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.extractors.RandomValueExtractor;
import com.pholser.junit.parameters.random.SourceOfRandomness;

public class FloatExtractor implements RandomValueExtractor<Float> {
    @Override
    public Float randomValue(SourceOfRandomness random) {
        return random.nextFloat();
    }
}

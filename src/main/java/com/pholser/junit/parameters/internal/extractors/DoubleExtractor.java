package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.extractors.RandomValueExtractor;
import com.pholser.junit.parameters.random.SourceOfRandomness;

public class DoubleExtractor implements RandomValueExtractor<Double> {
    @Override
    public Double randomValue(SourceOfRandomness random) {
        return random.nextDouble();
    }
}

package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.Between;

import com.pholser.junit.parameters.extractors.RandomValueExtractor;
import com.pholser.junit.parameters.random.SourceOfRandomness;

public class DoubleExtractor implements RandomValueExtractor<Double> {
    private Double min;
    private Double max;

    @Override
    public Double randomValue(SourceOfRandomness random) {
        if (min == null || max == null)
            return random.nextDouble();
        return min + (random.nextDouble() * (max - min));
    }

    @Override
    public boolean supportsRange() {
        return true;
    }

    @Override
    public void applyRange(Between range) {
        if (range != null) {
            min = new Double(range.min());
            max = new Double(range.max());
        }
    }
}

package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.Between;

import com.pholser.junit.parameters.extractors.RandomValueExtractor;
import com.pholser.junit.parameters.random.SourceOfRandomness;

public class FloatExtractor implements RandomValueExtractor<Float> {
    private Float min;
    private Float max;

    @Override
    public Float randomValue(SourceOfRandomness random) {
        if (min == null || max == null)
            return random.nextFloat();
        return min + (random.nextFloat() * (max - min));
    }

    @Override
    public boolean supportsRange() {
        return true;
    }

    @Override
    public void applyRange(Between range) {
        min = new Float(range.min());
        max = new Float(range.max());
    }
}

package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.Between;

import com.pholser.junit.parameters.extractors.RandomValueExtractor;
import com.pholser.junit.parameters.random.SourceOfRandomness;

public class IntegerExtractor implements RandomValueExtractor<Integer> {
    private Integer min;
    private Integer max;

    @Override
    public Integer randomValue(SourceOfRandomness random) {
        if (min == null || max == null)
            return random.nextInt();
        return random.nextInt(max - min + 1) + min;
    }

    @Override
    public boolean supportsRange() {
        return true;
    }

    @Override
    public void applyRange(Between range) {
        min = new Integer(range.min());
        max = new Integer(range.max());
    }
}

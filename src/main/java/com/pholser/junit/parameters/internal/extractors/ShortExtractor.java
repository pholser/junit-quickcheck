package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.Between;

import com.pholser.junit.parameters.extractors.RandomValueExtractor;
import com.pholser.junit.parameters.random.SourceOfRandomness;

public class ShortExtractor implements RandomValueExtractor<Short> {
    private Short min;
    private Short max;

    @Override
    public Short randomValue(SourceOfRandomness random) {
        if (min == null || max == null)
            return (short) random.nextInt();
        int result = random.nextInt(max - min + 1) + min;
        return (short) result;
    }

    @Override
    public boolean supportsRange() {
        return true;
    }

    @Override
    public void applyRange(Between range) {
        min = new Short(range.min());
        max = new Short(range.max());
    }
}

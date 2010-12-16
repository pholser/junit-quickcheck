package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.Between;

import com.pholser.junit.parameters.extractors.RandomValueExtractor;
import com.pholser.junit.parameters.random.SourceOfRandomness;

public class ByteExtractor implements RandomValueExtractor<Byte> {
    private Byte min;
    private Byte max;

    @Override
    public Byte randomValue(SourceOfRandomness random) {
        if (min == null || max == null)
            return (byte) random.nextInt();
        int result = random.nextInt(max - min + 1) + min;
        return (byte) result;
    }

    @Override
    public boolean supportsRange() {
        return true;
    }

    @Override
    public void applyRange(Between range) {
        min = new Byte(range.min());
        max = new Byte(range.max());
    }
}

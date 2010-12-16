package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.Between;

import com.pholser.junit.parameters.extractors.RandomValueExtractor;
import com.pholser.junit.parameters.random.SourceOfRandomness;

public class LongExtractor implements RandomValueExtractor<Long> {
    private Long min;
    private Long max;

    @Override
    public Long randomValue(SourceOfRandomness random) {
        if (min == null || max == null)
            return random.nextLong();
        return nextLong(random, max - min + 1) + min;
    }

    @Override
    public boolean supportsRange() {
        return true;
    }

    @Override
    public void applyRange(Between range) {
        if (range != null) {
            min = new Long(range.min());
            max = new Long(range.max());
        }
    }

    long nextLong(SourceOfRandomness random, long n) {
        long bits, result;

        do {
           bits = (random.nextLong() << 1) >>> 1;
           result = bits % n;
        } while (bits - result + (n - 1) < 0L);

        return result;
     }
}

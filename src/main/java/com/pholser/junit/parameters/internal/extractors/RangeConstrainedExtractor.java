package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.InRange;
import com.pholser.junit.parameters.extractors.RandomValueExtractor;
import com.pholser.junit.parameters.random.SourceOfRandomness;

public class RangeConstrainedExtractor implements RandomValueExtractor<Object> {
    private final RandomValueExtractor<?> wrapped;
    private final Integer min;
    private final Integer max;

    public RangeConstrainedExtractor(Class<?> targetType, RandomValueExtractor<?> wrapped, InRange range) {
        this.wrapped = wrapped;
        this.min = new Integer(range.min());
        this.max = new Integer(range.max());
    }

    @Override
    public Object randomValue(SourceOfRandomness random) {
        return random.nextInt(max - min + 1) + min;
    }
}

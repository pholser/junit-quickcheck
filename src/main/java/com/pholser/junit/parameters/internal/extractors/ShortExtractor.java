package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.extractors.RandomValueExtractor;
import com.pholser.junit.parameters.random.SourceOfRandomness;

public class ShortExtractor implements RandomValueExtractor<Short> {
    @Override
    public Short randomValue(SourceOfRandomness random) {
        return (short) random.nextInt();
    }
}

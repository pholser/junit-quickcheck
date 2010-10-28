package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.extractors.RandomValueExtractor;
import com.pholser.junit.parameters.random.SourceOfRandomness;

public class ByteExtractor implements RandomValueExtractor<Byte> {
    @Override
    public Byte randomValue(SourceOfRandomness random) {
        return (byte) random.nextInt();
    }
}

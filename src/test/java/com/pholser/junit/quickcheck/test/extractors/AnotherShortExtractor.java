package com.pholser.junit.quickcheck.test.extractors;

import com.pholser.junit.quickcheck.RandomValueExtractor;
import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;

public class AnotherShortExtractor extends RandomValueExtractor<Short> {
    public AnotherShortExtractor() {
        super(short.class);
    }

    @Override
    public Short extract(SourceOfRandomness random, int size) {
        return (short) size;
    }
}

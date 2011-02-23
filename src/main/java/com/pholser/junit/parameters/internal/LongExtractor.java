package com.pholser.junit.parameters.internal;

public class LongExtractor implements RandomValueExtractor<Long> {
    @Override
    public Long extract(SourceOfRandomness random) {
        return random.nextLong();
    }
}

package com.pholser.junit.parameters.internal;

public class BooleanExtractor implements RandomValueExtractor<Boolean> {
    @Override
    public Boolean extract(SourceOfRandomness random) {
        return random.nextBoolean();
    }
}

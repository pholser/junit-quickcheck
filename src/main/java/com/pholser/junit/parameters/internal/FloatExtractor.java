package com.pholser.junit.parameters.internal;

public class FloatExtractor implements RandomValueExtractor<Float> {
    @Override
    public Float extract(SourceOfRandomness random) {
        return random.nextFloat();
    }
}

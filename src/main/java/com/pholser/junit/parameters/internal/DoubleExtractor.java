package com.pholser.junit.parameters.internal;

public class DoubleExtractor implements RandomValueExtractor<Double> {
    @Override
    public Double extract(SourceOfRandomness random) {
        return random.nextDouble();
    }
}

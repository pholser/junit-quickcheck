package com.pholser.junit.parameters.internal;

public class IntegerExtractor implements RandomValueExtractor<Integer> {
    @Override
    public Integer extract(SourceOfRandomness random) {
        return random.nextInt();
    }
}

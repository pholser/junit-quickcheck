package com.pholser.junit.parameters.internal;

public interface RandomValueExtractor<T> {
    T extract(SourceOfRandomness random);
}

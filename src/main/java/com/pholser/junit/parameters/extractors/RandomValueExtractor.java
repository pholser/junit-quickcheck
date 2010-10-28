package com.pholser.junit.parameters.extractors;

import com.pholser.junit.parameters.random.SourceOfRandomness;

public interface RandomValueExtractor<T> {
    T randomValue(SourceOfRandomness random);
}

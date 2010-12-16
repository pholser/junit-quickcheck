package com.pholser.junit.parameters.extractors;

import com.pholser.junit.parameters.Between;
import com.pholser.junit.parameters.random.SourceOfRandomness;

public interface RandomValueExtractor<T> {
    T randomValue(SourceOfRandomness random);

    boolean supportsRange();

    void applyRange(Between range);
}

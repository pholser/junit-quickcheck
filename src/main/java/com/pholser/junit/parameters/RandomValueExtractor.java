package com.pholser.junit.parameters;

interface RandomValueExtractor {
    Object randomValue(SourceOfRandomness random);
}

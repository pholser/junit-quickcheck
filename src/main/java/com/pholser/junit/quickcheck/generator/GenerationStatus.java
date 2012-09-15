package com.pholser.junit.quickcheck.generator;

public interface GenerationStatus {
    int size();

    int successes();

    int discards();
}

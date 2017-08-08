package com.pholser.junit.quickcheck.internal.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class GuaranteeValuesGenerator extends Generator<Object> {
    private final ExhaustiveDomainGenerator guaranteed;
    private final Generator<?> rest;

    public GuaranteeValuesGenerator(
        ExhaustiveDomainGenerator guaranteed,
        Generator<?> rest) {

        super(Object.class);

        this.guaranteed = guaranteed;
        this.rest = rest;
    }

    @Override public Object generate(SourceOfRandomness random, GenerationStatus status) {
        return guaranteed.hasNext()
            ? guaranteed.generate(random, status)
            : rest.generate(random, status);
    }
}

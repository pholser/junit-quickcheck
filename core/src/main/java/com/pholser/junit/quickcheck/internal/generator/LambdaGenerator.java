package com.pholser.junit.quickcheck.internal.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static com.pholser.junit.quickcheck.generator.Lambdas.*;

public class LambdaGenerator<T, U> extends Generator<Object> {
    private final Class<T> lambdaType;
    private final Generator<U> returnValueGenerator;

    public LambdaGenerator(Class<T> lambdaType, Generator<U> returnValueGenerator) {
        super(Object.class);

        this.lambdaType = lambdaType;
        this.returnValueGenerator = returnValueGenerator;
    }

    @Override
    public T generate(SourceOfRandomness random, GenerationStatus status) {
        return makeLambda(lambdaType, returnValueGenerator, status);
    }
}

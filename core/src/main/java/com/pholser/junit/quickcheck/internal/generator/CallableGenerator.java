package com.pholser.junit.quickcheck.internal.generator;

import java.util.concurrent.Callable;

import com.pholser.junit.quickcheck.generator.ComponentizedGenerator;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static com.pholser.junit.quickcheck.generator.Lambdas.*;

public class CallableGenerator<V> extends ComponentizedGenerator<Callable> {
    public CallableGenerator() {
        super(Callable.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Callable<V> generate(SourceOfRandomness random, GenerationStatus status) {
        return (Callable<V>) makeLambda(Callable.class, componentGenerators.get(0), status);
    }

    @Override
    public int numberOfNeededComponents() {
        return 1;
    }
}

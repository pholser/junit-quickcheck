package com.pholser.junit.quickcheck.test.generator;

import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;

public class FooGenerator extends Generator<Foo> {
    public FooGenerator() {
        super(Foo.class);
    }

    @Override
    public Foo generate(SourceOfRandomness random, int size) {
        return new Foo(size);
    }
}

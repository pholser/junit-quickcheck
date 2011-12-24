package com.pholser.junit.quickcheck.test.extractors;

import com.pholser.junit.quickcheck.RandomValueExtractor;
import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;

public class FooExtractor extends RandomValueExtractor<Foo> {
    public FooExtractor() {
        super(Foo.class);
    }

    @Override
    public Foo extract(SourceOfRandomness random, int size) {
        return new Foo(size);
    }
}

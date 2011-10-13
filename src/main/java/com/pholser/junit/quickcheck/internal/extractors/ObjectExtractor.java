package com.pholser.junit.quickcheck.internal.extractors;

import com.pholser.junit.quickcheck.RegisterableRandomValueExtractor;
import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;

public class ObjectExtractor extends RegisterableRandomValueExtractor<Object> {
    public ObjectExtractor() {
        super(Object.class);
    }

    @Override
    public Object extract(SourceOfRandomness random) {
        return random.nextInt();
    }
}

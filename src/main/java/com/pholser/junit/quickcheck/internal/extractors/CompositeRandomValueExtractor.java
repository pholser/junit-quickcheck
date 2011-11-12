package com.pholser.junit.quickcheck.internal.extractors;

import java.util.ArrayList;
import java.util.List;

import com.pholser.junit.quickcheck.RandomValueExtractor;
import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;

public class CompositeRandomValueExtractor extends RandomValueExtractor<Object> {
    final List<RandomValueExtractor<?>> components;

    public CompositeRandomValueExtractor(List<RandomValueExtractor<?>> components) {
        super(Object.class);

        this.components = new ArrayList<RandomValueExtractor<?>>(components);
    }

    @Override
    public Object extract(SourceOfRandomness random) {
        RandomValueExtractor<?> extractor = components.size() == 1
            ? components.get(0)
            : components.get(random.nextInt(0, components.size() - 1));

        return extractor.extract(random);
    }
}

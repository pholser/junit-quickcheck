package com.pholser.junit.parameters.internal;

import java.util.ArrayList;
import java.util.List;

public class ListExtractor implements RandomValueExtractor<List<?>> {
    private final RandomValueExtractor<?> componentExtractor;

    public ListExtractor(RandomValueExtractor<?> componentExtractor) {
        this.componentExtractor = componentExtractor;
    }

    @Override
    public List<?> extract(SourceOfRandomness random) {
        int size = random.nextInt(0, 100);
        List<Object> items = new ArrayList<Object>();
        for (int i = 0; i < size; ++i)
            items.add(componentExtractor.extract(random));
        return items;
    }
}

package com.pholser.junit.parameters.internal;

import java.lang.reflect.Array;

public class ArrayExtractor implements RandomValueExtractor<Object> {
    private final Class<?> componentType;
    private final RandomValueExtractor<?> componentExtractor;

    public ArrayExtractor(Class<?> componentType, RandomValueExtractor<?> componentExtractor) {
        this.componentType = componentType;
        this.componentExtractor = componentExtractor;
    }

    @Override
    public Object extract(SourceOfRandomness random) {
        int length = random.nextInt(0, 100);
        Object array = Array.newInstance(componentType, length);
        for (int i = 0; i < length; ++i)
            Array.set(array, i, componentExtractor.extract(random));
        return array;
    }
}

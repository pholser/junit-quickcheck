package com.pholser.junit.parameters.extractors;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class ServiceLoaderExtractorSource implements RandomValueExtractorSource {
    @Override
    public Map<Class<?>, RandomValueExtractor<?>> extractors() {
        Map<Class<?>, RandomValueExtractor<?>> extractors = new HashMap<Class<?>, RandomValueExtractor<?>>();
        for (RandomValueExtractor<?> each : ServiceLoader.load(RandomValueExtractor.class))
            addExtractor(extractors, each);
        return extractors;
    }

    private void addExtractor(Map<Class<?>, RandomValueExtractor<?>> extractors,
        RandomValueExtractor<?> newExtractor) {

        for (Type each : newExtractor.getClass().getGenericInterfaces()) {
            if (each instanceof ParameterizedType) {
                ParameterizedType parameterized = (ParameterizedType) each;
                if (RandomValueExtractor.class.equals(parameterized.getRawType())) {
                    Type typeArgument = parameterized.getActualTypeArguments()[0];
                    if (typeArgument instanceof Class<?>)
                        extractors.put((Class<?>) typeArgument, newExtractor);
                }
            }
        }
    }
}

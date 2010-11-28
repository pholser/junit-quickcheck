package com.pholser.junit.parameters.extractors;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class ServiceLoaderExtractorSource implements RandomValueExtractorSource {
    @Override
    public Map<Type, RandomValueExtractor<?>> extractors() {
        Map<Type, RandomValueExtractor<?>> extractors = new HashMap<Type, RandomValueExtractor<?>>();
        for (RandomValueExtractor<?> each : ServiceLoader.load(RandomValueExtractor.class))
            extractors.put(targetType(each), each);
        return extractors;
    }

    Type targetType(RandomValueExtractor<?> extractor) {
        for (Type each : extractor.getClass().getGenericInterfaces()) {
            if (each instanceof ParameterizedType) {
                ParameterizedType parameterized = (ParameterizedType) each;
                if (RandomValueExtractor.class.equals(parameterized.getRawType()))
                    return parameterized.getActualTypeArguments()[0];
            }
        }
        return null;
    }
}

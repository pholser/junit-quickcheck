package com.pholser.junit.quickcheck.internal.extractors;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javaruntype.type.Types;

import com.pholser.junit.quickcheck.RandomValueExtractor;
import com.pholser.junit.quickcheck.RegisterableRandomValueExtractor;

public class ExtractorRepository {
    private final Map<Type, RandomValueExtractor<?>> extractors = new HashMap<Type, RandomValueExtractor<?>>();

    public ExtractorRepository add(Iterable<RegisterableRandomValueExtractor<?>> source) {
        for (RegisterableRandomValueExtractor<?> each : source)
            registerTypes(each);

        return this;
    }

    private void registerTypes(RegisterableRandomValueExtractor<?> extractor) {
        for (Class<?> each : extractor.types())
            extractors.put(each, extractor);
    }

    public RandomValueExtractor<?> extractorFor(Type type) {
        org.javaruntype.type.Type<?> typeToken = Types.forJavaLangReflectType(type);

        if (typeToken.isArray()) {
            Class<?> componentType = typeToken.getComponentClass();
            return new ArrayExtractor(componentType, extractors.get(componentType));
        } else if (List.class.equals(typeToken.getRawClass())) {
            Class<?> componentType = typeToken.getTypeParameters().get(0).getType().getRawClass();
            return new ListExtractor(extractors.get(componentType));
        }

        return extractors.get(type);
    }
}

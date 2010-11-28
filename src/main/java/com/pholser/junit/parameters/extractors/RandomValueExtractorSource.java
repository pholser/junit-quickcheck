package com.pholser.junit.parameters.extractors;

import java.lang.reflect.Type;
import java.util.Map;

public interface RandomValueExtractorSource {
    Map<Type, RandomValueExtractor<?>> extractors();
}

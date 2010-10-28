package com.pholser.junit.parameters.extractors;

import java.util.Map;

public interface RandomValueExtractorSource {
    Map<Class<?>, RandomValueExtractor<?>> extractors();
}

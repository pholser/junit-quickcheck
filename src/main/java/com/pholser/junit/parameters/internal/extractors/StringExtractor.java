package com.pholser.junit.parameters.internal.extractors;

import java.io.UnsupportedEncodingException;

import com.pholser.junit.parameters.extractors.RandomValueExtractor;
import com.pholser.junit.parameters.random.SourceOfRandomness;

public class StringExtractor implements RandomValueExtractor<String> {
    private final String encoding;

    public StringExtractor() {
        this("US-ASCII");
    }

    public StringExtractor(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public String randomValue(SourceOfRandomness random) {
        try {
            return new String(random.nextBytes(16), encoding);
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError(ex);
        }
    }
}

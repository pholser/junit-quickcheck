package com.pholser.junit.quickcheck.internal.extractors;

import java.util.Arrays;

import com.pholser.junit.quickcheck.RegisterableRandomValueExtractor;

public class BasicExtractors {
    private BasicExtractors() {
        throw new UnsupportedOperationException();
    }

    public static Iterable<RegisterableRandomValueExtractor> extractors() {
        return Arrays.<RegisterableRandomValueExtractor> asList(
            new BigDecimalExtractor(),
            new BigIntegerExtractor(),
            new BooleanExtractor(),
            new ByteExtractor(),
            new CharacterExtractor(),
            new DoubleExtractor(),
            new FloatExtractor(),
            new IntegerExtractor(),
            new LongExtractor(),
            new ShortExtractor());
    }
}

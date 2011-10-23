package com.pholser.junit.quickcheck.internal.extractors;

import com.pholser.junit.quickcheck.RegisterableRandomValueExtractor;
import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;

public class StringExtractor extends RegisterableRandomValueExtractor<String> {
    private final CharacterExtractor charExtractor = new CharacterExtractor();

    public StringExtractor() {
        super(String.class);
    }

    @Override
    public String extract(SourceOfRandomness random) {
        int size = random.nextInt(0, 100);
        StringBuilder buffer = new StringBuilder(size);
        for (int i = 0; i < size; ++i)
            buffer.append(charExtractor.extract(random));

        return buffer.toString();
    }
}

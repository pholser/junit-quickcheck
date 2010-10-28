package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.extractors.RandomValueExtractor;
import com.pholser.junit.parameters.random.SourceOfRandomness;

public class CharacterExtractor implements RandomValueExtractor<Character> {
    @Override
    public Character randomValue(SourceOfRandomness random) {
        return (char) random.nextInt();
    }
}

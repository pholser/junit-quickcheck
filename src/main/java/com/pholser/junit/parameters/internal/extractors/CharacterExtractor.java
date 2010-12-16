package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.Between;

import com.pholser.junit.parameters.extractors.RandomValueExtractor;
import com.pholser.junit.parameters.random.SourceOfRandomness;

public class CharacterExtractor implements RandomValueExtractor<Character> {
    private Character min;
    private Character max;

    @Override
    public Character randomValue(SourceOfRandomness random) {
        if (min == null || max == null)
            return (char) random.nextInt();
        int result = random.nextInt(max - min + 1) + min;
        return (char) result;
    }

    @Override
    public boolean supportsRange() {
        return true;
    }

    @Override
    public void applyRange(Between range) {
        min = new Character(range.min().charAt(0));
        max = new Character(range.min().charAt(0));
    }
}

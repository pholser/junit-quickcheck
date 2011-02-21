package com.pholser.junit.parameters.internal;

import static java.lang.Character.*;

public class CharExtractor implements RandomValueExtractor<Character> {
    @Override
    public Character extract(SourceOfRandomness random) {
        return (char) random.nextInt(MIN_VALUE, MAX_VALUE);
    }
}

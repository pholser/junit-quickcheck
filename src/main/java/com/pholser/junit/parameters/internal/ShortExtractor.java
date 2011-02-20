package com.pholser.junit.parameters.internal;

import static java.lang.Short.*;

public class ShortExtractor implements RandomValueExtractor<Short> {
    @Override
    public Short extract(SourceOfRandomness random) {
        return (short) random.nextInt(MIN_VALUE, MAX_VALUE);
    }
}

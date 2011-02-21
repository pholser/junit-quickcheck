package com.pholser.junit.parameters.internal;

import static java.lang.Byte.*;

public class ByteExtractor implements RandomValueExtractor<Byte> {
    @Override
    public Byte extract(SourceOfRandomness random) {
        return (byte) random.nextInt(MIN_VALUE, MAX_VALUE);
    }
}

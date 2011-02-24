package com.pholser.junit.parameters.internal;

import java.math.BigInteger;

public class BigIntegerExtractor implements RandomValueExtractor<BigInteger> {
    @Override
    public BigInteger extract(SourceOfRandomness random) {
        int size = random.nextInt(0, 100);
        byte[] bytes = random.nextBytes(size);
        return new BigInteger(bytes);
    }
}

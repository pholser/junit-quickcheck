package com.pholser.junit.parameters.internal;

import java.math.BigDecimal;

public class BigDecimalExtractor implements RandomValueExtractor<BigDecimal> {
    private final BigIntegerExtractor bigIntegerExtractor;

    public BigDecimalExtractor() {
        this(new BigIntegerExtractor());
    }

    protected BigDecimalExtractor(BigIntegerExtractor bigIntegerExtractor) {
        this.bigIntegerExtractor = bigIntegerExtractor;
    }

    @Override
    public BigDecimal extract(SourceOfRandomness random) {
        return new BigDecimal(bigIntegerExtractor.extract(random), random.nextInt());
    }
}

package com.pholser.junit.quickcheck.internal;

import com.pholser.junit.quickcheck.internal.generator.PropertyParameterGenerationContext;

public final class SeededValue {
    private final PropertyParameterGenerationContext p;
    private final Object value;
    private final long seed;

    public SeededValue(PropertyParameterGenerationContext p) {
        this.p = p;
        this.value = p.generate();
        this.seed = p.effectiveSeed();
    }

    public PropertyParameterGenerationContext parameter() {
        return p;
    }

    public Object value() {
        return value;
    }

    public long seed() {
        return seed;
    }
}

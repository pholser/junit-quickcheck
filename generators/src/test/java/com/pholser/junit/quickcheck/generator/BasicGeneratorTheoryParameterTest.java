package com.pholser.junit.quickcheck.generator;

import com.pholser.junit.quickcheck.internal.generator.CoreTheoryParameterTest;
import com.pholser.junit.quickcheck.internal.generator.ServiceLoaderGeneratorSource;

public abstract class BasicGeneratorTheoryParameterTest extends CoreTheoryParameterTest {
    @Override protected Iterable<Generator<?>> generatorSource() {
        return new ServiceLoaderGeneratorSource();
    }
}

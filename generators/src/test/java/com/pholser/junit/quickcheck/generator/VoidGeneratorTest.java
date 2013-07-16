package com.pholser.junit.quickcheck.generator;

import org.junit.Test;

import static junit.framework.Assert.*;

public class VoidGeneratorTest {
    @Test public void canOnlyGenerateNull() {
        VoidGenerator generator = new VoidGenerator();

        Void value = generator.generate(null, null);

        assertNull(value);
    }
}

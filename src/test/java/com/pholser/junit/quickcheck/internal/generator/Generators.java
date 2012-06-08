package com.pholser.junit.quickcheck.internal.generator;

import com.pholser.junit.quickcheck.generator.Generator;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

public class Generators {
    private Generators() {
        throw new UnsupportedOperationException();
    }

    public static void assertGenerators(Generator<?> result, Class<? extends Generator>... expectedTypes) {
        assumeThat(result, is(CompositeGenerator.class));

        CompositeGenerator composite = (CompositeGenerator) result;
        for (int i = 0; i < expectedTypes.length; ++i)
            assertThat("generators[" + i + ']', composite.componentGenerator(i), is(expectedTypes[i]));
    }
}

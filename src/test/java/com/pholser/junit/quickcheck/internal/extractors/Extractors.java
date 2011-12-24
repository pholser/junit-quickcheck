package com.pholser.junit.quickcheck.internal.extractors;

import com.pholser.junit.quickcheck.RandomValueExtractor;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

public class Extractors {
    private Extractors() {
        throw new UnsupportedOperationException();
    }

    public static void assertExtractors(RandomValueExtractor<?> result,
        Class<? extends RandomValueExtractor>... expectedTypes) {

        assumeThat(result, is(CompositeRandomValueExtractor.class));

        CompositeRandomValueExtractor composite = (CompositeRandomValueExtractor) result;
        assertEquals(expectedTypes.length, composite.components.size());
        for (int i = 0; i < expectedTypes.length; ++i)
            assertThat("extractors[" + i, composite.components.get(i), is(expectedTypes[i]));
    }
}

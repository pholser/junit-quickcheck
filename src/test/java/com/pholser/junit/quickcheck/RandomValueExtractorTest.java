package com.pholser.junit.quickcheck;

import java.util.Collections;

import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RandomValueExtractorTest {
    private RandomValueExtractor<Object> extractor;

    @Before
    public void setUp() {
        extractor = new RandomValueExtractor<Object>(Object.class) {
            @Override
            public Object extract(SourceOfRandomness random) {
                return this;
            }
        };
    }

    @Test
    public void noComponents() {
        assertFalse(extractor.hasComponents());
    }

    @Test
    public void addingComponentsDoesNothing() {
        extractor.addComponentExtractors(Collections.<RandomValueExtractor<?>> emptyList());
    }
}

package com.pholser.junit.parameters.extractors;

import java.io.Serializable;
import java.util.concurrent.Callable;

import org.junit.Before;

import static org.junit.Assert.*;

import com.pholser.junit.parameters.extractors.ServiceLoaderExtractorSource;

import com.pholser.junit.parameters.extractors.RandomValueExtractor;
import com.pholser.junit.parameters.random.SourceOfRandomness;
import org.junit.Test;

public class AnalyzingTypesToDetermineExtractedTypeTest {
    private ServiceLoaderExtractorSource source;

    @Before
    public void setUp() {
        source = new ServiceLoaderExtractorSource();
    }

    @Test
    public void withOtherImplementedRegularInterfaces() {
        assertEquals(Integer.class, source.targetType(new OtherImplementedRegularInterfaces()));
    }

    public static class OtherImplementedRegularInterfaces
        implements Serializable, RandomValueExtractor<Integer> {

        private static final long serialVersionUID = 1L;

        @Override
        public Integer randomValue(SourceOfRandomness random) {
            return null;
        }
    }

    @Test
    public void withOtherImplementedGenericInterfaces() {
        assertEquals(Integer.class, source.targetType(new OtherImplementedRegularInterfaces()));
    }

    public static class OtherImplementedGenericInterfaces
        implements Callable<Integer>, RandomValueExtractor<String> {

        @Override
        public String randomValue(SourceOfRandomness random) {
            return null;
        }

        @Override
        public Integer call() {
            return null;
        }
    }
}

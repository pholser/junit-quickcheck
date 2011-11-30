package com.pholser.junit.quickcheck.internal.generate;

import java.lang.reflect.Type;
import java.util.List;

import static java.util.Arrays.*;

import com.pholser.junit.quickcheck.RandomValueExtractor;
import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;

import static org.mockito.Mockito.*;

public class ExplicitExtractorsChosenWithEqualProbabilityTest extends GeneratingUniformRandomValuesForTheoryParameterTest{
    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextInt(0, 2)).thenReturn(0).thenReturn(1).thenReturn(2);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends RandomValueExtractor>[] explicitExtractors() {
        return new Class[] { FooExtractor.class, BarExtractor.class, BazExtractor.class };
    }

    @Override
    protected Type parameterType() {
        return String.class;
    }

    @Override
    protected int sampleSize() {
        return 3;
    }

    @Override
    protected List<?> randomValues() {
        return asList("foo", "bar", "baz");
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random, times(3)).nextInt(0, 2);
    }

    public static class FooExtractor extends RandomValueExtractor<String> {
        public FooExtractor() {
            super(String.class);
        }

        @Override
        public String extract(SourceOfRandomness random) {
            return "foo";
        }
    }

    public static class BarExtractor extends RandomValueExtractor<String> {
        public BarExtractor() {
            super(String.class);
        }

        @Override
        public String extract(SourceOfRandomness random) {
            return "bar";
        }
    }

    public static class BazExtractor extends RandomValueExtractor<String> {
        public BazExtractor() {
            super(String.class);
        }

        @Override
        public String extract(SourceOfRandomness random) {
            return "baz";
        }
    }
}

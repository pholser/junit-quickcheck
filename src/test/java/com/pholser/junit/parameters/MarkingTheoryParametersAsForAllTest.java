package com.pholser.junit.parameters;

import static com.pholser.junit.parameters.Annotations.*;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class MarkingTheoryParametersAsForAllTest {
    @Test
    public void shouldFeedADefaultNumberOfValuesToAMarkedParameter() throws Exception {
        assertThat(testResult(ForDefaultNumberOfValues.class), isSuccessful());
        assertEquals(defaultValueFor(ForAll.class, "sampleSize"), ForDefaultNumberOfValues.iterations);
    }

    @RunWith(Theories.class)
    public static class ForDefaultNumberOfValues {
        static int iterations;

        @Theory
        public void shouldHold(@ForAll int i) {
            ++iterations;
        }
    }

    @Test
    public void shouldRespectSampleSizeIfSpecified() {
        assertThat(testResult(ForSpecifiedNumberOfValues.class), isSuccessful());
        assertEquals(5, ForSpecifiedNumberOfValues.iterations);
    }

    @RunWith(Theories.class)
    public static class ForSpecifiedNumberOfValues {
        static int iterations;

        @Theory
        public void shouldHold(@ForAll(sampleSize = 5) int i) {
            ++iterations;
        }
    }

    @Test
    public void shouldBeAbleToMarkMultipleParametersForReceivingValues() {
        assertThat(testResult(ForValuesOfMultipleParameters.class), isSuccessful());
        assertEquals(21, ForValuesOfMultipleParameters.iterations);
    }

    @RunWith(Theories.class)
    public static class ForValuesOfMultipleParameters {
        static int iterations;

        @Theory
        public void shouldHold(@ForAll(sampleSize = 3) int i, @ForAll(sampleSize = 7) int j) {
            ++iterations;
        }
    }
}

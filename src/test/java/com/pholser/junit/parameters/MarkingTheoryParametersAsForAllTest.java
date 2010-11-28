package com.pholser.junit.parameters;

import java.util.Date;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class MarkingTheoryParametersAsForAllTest {
    @Test
    public void shouldFeedDefaultNumberOfRandomIntsToAMarkedIntParameter() throws Exception {
        assertThat(testResult(ForDefaultNumberOfInts.class), isSuccessful());
        assertEquals(defaultSampleSize(), ForDefaultNumberOfInts.iterations);
    }

    @RunWith(Theories.class)
    public static class ForDefaultNumberOfInts {
        static int iterations;

        @Theory
        public void shouldHold(@ForAll int i) {
            ++iterations;
        }
    }

    @Test
    public void shouldFeedDefaultNumberOfRandomDoublesToAMarkedDoubleParameter() throws Exception {
        assertThat(testResult(ForDefaultNumberOfDoubles.class), isSuccessful());
        assertEquals(defaultSampleSize(), ForDefaultNumberOfDoubles.iterations);
    }

    @RunWith(Theories.class)
    public static class ForDefaultNumberOfDoubles {
        static int iterations;

        @Theory
        public void shouldHold(@ForAll double d) {
            ++iterations;
        }
    }

    @Test
    public void shouldFeedDefaultNumberOfRandomFloatsToAMarkedStringParameter() throws Exception {
        assertThat(testResult(ForDefaultNumberOfFloats.class), isSuccessful());
        assertEquals(defaultSampleSize(), ForDefaultNumberOfFloats.iterations);
    }

    @RunWith(Theories.class)
    public static class ForDefaultNumberOfFloats {
        static int iterations;

        @Theory
        public void shouldHold(@ForAll float f) {
            ++iterations;
        }
    }

    @Test
    public void shouldFeedDefaultNumberOfBooleansToAMarkedBooleanParameter() throws Exception {
        assertThat(testResult(ForDefaultNumberOfBooleans.class), isSuccessful());
        assertEquals(defaultSampleSize(), ForDefaultNumberOfBooleans.iterations);
    }

    @RunWith(Theories.class)
    public static class ForDefaultNumberOfBooleans {
        static int iterations;

        @Theory
        public void shouldHold(@ForAll boolean b) {
            ++iterations;
        }
    }

    @Test
    public void shouldFeedDefaultNumberOfBooleansWrappersToAMarkedBooleanWrapperParameter() throws Exception {
        assertThat(testResult(ForDefaultNumberOfBooleanWrappers.class), isSuccessful());
        assertEquals(defaultSampleSize(), ForDefaultNumberOfBooleanWrappers.iterations);
    }

    @RunWith(Theories.class)
    public static class ForDefaultNumberOfBooleanWrappers {
        static int iterations;

        @Theory
        public void shouldHold(@ForAll Boolean b) {
            ++iterations;
        }
    }

    @Test
    public void shouldFeedDefaultNumberOfRandomStringsToAMarkedStringParameter() throws Exception {
        assertThat(testResult(ForDefaultNumberOfStrings.class), isSuccessful());
        assertEquals(defaultSampleSize(), ForDefaultNumberOfStrings.iterations);
    }

    @RunWith(Theories.class)
    public static class ForDefaultNumberOfStrings {
        static int iterations;

        @Theory
        public void shouldHold(@ForAll String s) {
            ++iterations;
        }
    }

    @Test
    public void shouldAllowDifferentNumberOfRandomValuesOnAMarkedParameter() {
        assertThat(testResult(ForSpecifiedNumberOfStrings.class), isSuccessful());
        assertEquals(200, ForSpecifiedNumberOfStrings.iterations);
    }

    @RunWith(Theories.class)
    public static class ForSpecifiedNumberOfStrings {
        static int iterations;

        @Theory
        public void shouldHold(@ForAll(sampleSize = 200) String s) {
            ++iterations;
        }
    }

    @Test
    public void shouldAllowMultipleForAllParmsOnATheoryMethod() {
        assertThat(testResult(MultipleForAlls.class), isSuccessful());
        assertEquals(15, MultipleForAlls.iterations);
    }

    @RunWith(Theories.class)
    public static class MultipleForAlls {
        static int iterations;

        @Theory
        public void shouldHold(@ForAll(sampleSize = 3) int i, @ForAll(sampleSize = 5) int j) {
            ++iterations;
        }
    }

    @Test
    public void shouldFeedRandomDatesToAMarkedDateParameter() throws Exception {
        assertThat(testResult(ForDefaultNumberOfDates.class), isSuccessful());
        assertEquals(defaultSampleSize(), ForDefaultNumberOfDates.iterations);
    }

    @RunWith(Theories.class)
    public static class ForDefaultNumberOfDates {
        static int iterations;

        @Theory
        public void shouldHold(@ForAll Date d) {
            ++iterations;
        }
    }

    @Test
    public void shouldRejectMarkedParametersOfUnsupportedType() {
        assertThat(testResult(ForUnsupportedType.class),
            hasSingleFailureContaining("Don't know how to generate values of " + Void.class));
    }

    @RunWith(Theories.class)
    public static class ForUnsupportedType {
        @Theory
        public void shouldHold(@ForAll Void v) {
            // empty on purpose
        }
    }

    private static int defaultSampleSize() throws NoSuchMethodException {
        return (Integer) ForAll.class.getMethod("sampleSize").getDefaultValue();
    }
}

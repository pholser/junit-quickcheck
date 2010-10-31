package com.pholser.junit.parameters;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;

public class MarkingTheoryParametersAsForAllTest {
    @Test
    public void shouldFeedDefaultNumberOfRandomIntsToAMarkedIntParameter() throws Exception {
        JUnitCore.runClasses(ForDefaultNumberOfInts.class);

        assertEquals(ForAll.class.getMethod("sampleSize").getDefaultValue(),
            ForDefaultNumberOfInts.iterations);
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
        JUnitCore.runClasses(ForDefaultNumberOfDoubles.class);

        assertEquals(ForAll.class.getMethod("sampleSize").getDefaultValue(),
            ForDefaultNumberOfDoubles.iterations);
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
        JUnitCore.runClasses(ForDefaultNumberOfFloats.class);

        assertEquals(ForAll.class.getMethod("sampleSize").getDefaultValue(), ForDefaultNumberOfFloats.iterations);
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
        JUnitCore.runClasses(ForDefaultNumberOfBooleans.class);

        assertEquals(ForAll.class.getMethod("sampleSize").getDefaultValue(),
            ForDefaultNumberOfBooleans.iterations);
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
        JUnitCore.runClasses(ForDefaultNumberOfBooleanWrappers.class);

        assertEquals(ForAll.class.getMethod("sampleSize").getDefaultValue(),
            ForDefaultNumberOfBooleanWrappers.iterations);
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
        JUnitCore.runClasses(ForDefaultNumberOfStrings.class);

        assertEquals(ForAll.class.getMethod("sampleSize").getDefaultValue(), ForDefaultNumberOfStrings.iterations);
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
        JUnitCore.runClasses(ForSpecifiedNumberOfStrings.class);

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
        JUnitCore.runClasses(MultipleForAlls.class);

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
        JUnitCore.runClasses(ForDefaultNumberOfDates.class);

        assertEquals(ForAll.class.getMethod("sampleSize").getDefaultValue(),
            ForDefaultNumberOfDates.iterations);
    }

    @RunWith(Theories.class)
    public static class ForDefaultNumberOfDates {
        static int iterations;

        @Theory
        public void shouldHold(@ForAll Date d) {
            ++iterations;
        }
    }
}

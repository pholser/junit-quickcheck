package com.pholser.junit.parameters;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;

public class WhenMarkingTheoryParametersAsForAll {
    @Test
    public void shouldFeedDefaultNumberOfRandomIntsToAMarkedIntParameter() throws Exception {
        JUnitCore.runClasses(ForDefaultNumberOfInts.class);

        assertEquals(ForAll.class.getMethod("sampleSize").getDefaultValue(), ForDefaultNumberOfInts.iterations);
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

        assertEquals(ForAll.class.getMethod("sampleSize").getDefaultValue(), ForDefaultNumberOfDoubles.iterations);
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
        JUnitCore.runClasses(ForDefaultNumberOfStrings.class);

        assertEquals(200, ForDefaultNumberOfStrings.iterations);
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
    public void shouldAllowNonPrimitiveTypesToBeRandomlyGenerated() {
        JUnitCore.runClasses(NonPrimitiveParameter.class);

        assertEquals(100, RandomDateExtractor.numberOfCalls);
    }

    @RunWith(Theories.class)
    public static class NonPrimitiveParameter {
        @Theory
        public void shouldHold(@ForAll @ExtractedBy(RandomDateExtractor.class) Date d) {
            assertNotNull(d);
        }
    }

    static class RandomDateExtractor implements RandomValueExtractor {
        static int numberOfCalls;

        @Override
        public Object randomValue(SourceOfRandomness random) {
            ++numberOfCalls;
            return new Date(random.nextLong());
        }
    }
}

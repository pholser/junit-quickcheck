package com.pholser.junit.quickcheck.guava.generator;

import com.google.common.base.Function;
import com.pholser.junit.quickcheck.ForAll;
import org.junit.Test;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Annotations.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ForAllFunctionTheoryParameterTypesTest {
    @Test
    public void stringToInteger() throws Exception {
        assertThat(testResult(StringToInteger.class), isSuccessful());
        assertEquals(defaultSampleSize(), StringToInteger.iterations);
    }

    @RunWith(Theories.class)
    public static class StringToInteger {
        static int iterations;

        @Theory
        public void shouldHold(@ForAll Function<String, Integer> f) {
            ++iterations;

            Integer result = f.apply("foo");
            for (int i = 0; i < 10000; ++i)
                assertEquals(result, f.apply("foo"));
        }
    }

    @Test
    public void superShortToInteger() throws Exception {
        assertThat(testResult(SuperShortToExtendsInteger.class), isSuccessful());
        assertEquals(defaultSampleSize(), SuperShortToExtendsInteger.iterations);
    }

    @RunWith(Theories.class)
    public static class SuperShortToExtendsInteger {
        static int iterations;

        @Theory
        public void shouldHold(@ForAll Function<? super Short, ? extends Integer> f) {
            ++iterations;

            Integer result = f.apply((short) 4);
            for (int i = 0; i < 10000; ++i)
                assertEquals(result, f.apply((short) 4));
        }
    }

    @Test
    public void arrayOfFunction() throws Exception {
        assertThat(testResult(ArrayOfFunction.class), isSuccessful());
        assertEquals(defaultSampleSize(), SuperShortToExtendsInteger.iterations);
    }

    @RunWith(Theories.class)
    public static class ArrayOfFunction {
        static int iterations;

        @SuppressWarnings("unchecked")
        @Theory
        public void shouldHold(@ForAll Function<?, ?>[] functions) {
            ++iterations;

            for (Function each : functions) {
                each.apply("foo");
            }
        }
    }
}

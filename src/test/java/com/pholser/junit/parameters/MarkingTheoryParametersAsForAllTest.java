package com.pholser.junit.parameters;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class MarkingTheoryParametersAsForAllTest {
    @Test
    public void shouldFeedDefaultNumberOfRandomIntsToAMarkedIntParameter() {
        assertThat(testResult(ForDefaultNumberOfInts.class), isSuccessful());
        assertEquals(100, ForDefaultNumberOfInts.iterations);
    }

    @RunWith(Theories.class)
    public static class ForDefaultNumberOfInts {
        static int iterations;

        @Theory
        public void shouldHold(@ForAll int i) {
            ++iterations;
        }
    }
}

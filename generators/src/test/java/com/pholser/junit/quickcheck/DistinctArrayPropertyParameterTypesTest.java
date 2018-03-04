package com.pholser.junit.quickcheck;

import com.google.common.primitives.Ints;
import com.pholser.junit.quickcheck.generator.Distinct;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.internal.Lists;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class DistinctArrayPropertyParameterTypesTest {
    @Test public void distinctArrays() {
        assertThat(testResult(DistinctArrays.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class DistinctArrays {
        @Property public void shouldHold(
            @InRange(minInt = 1, maxInt = 5)int @Size(min = 2, max = 5) @Distinct [] i) {
            assertThat(i.length, allOf(greaterThanOrEqualTo(2), lessThanOrEqualTo(5)));
            assertThat(Lists.isDistinct(Ints.asList(i)), is(true));
        }
    }

    @Test public void shrinkingDistinctArrays() {
        assertThat(testResult(ShrinkingDistinctArrays.class), failureCountIs(1));
        assertThat(
            ShrinkingDistinctArrays.failed.length,
            allOf(greaterThanOrEqualTo(4), lessThanOrEqualTo(5)));
        assertThat(Lists.isDistinct(Ints.asList(ShrinkingDistinctArrays.failed)), is(true));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingDistinctArrays {
        static int[] failed;

        @Property public void shouldHold(
            @InRange(minInt = 1, maxInt = 5) int @Size(min = 4, max = 5) @Distinct [] i) {
            failed = i;

            fail();
        }
    }
}

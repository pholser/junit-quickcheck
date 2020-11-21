package com.pholser.junit.quickcheck;

import com.pholser.junit.quickcheck.generator.Distinct;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class DistinctSetPropertyParameterTypesTest {
    @Test public void distinctSets() {
        assertThat(testResult(DistinctSets.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class DistinctSets {
        @Property public void shouldHold(
            @Size(min = 4, max = 5)
            @Distinct
                Set<@InRange(minInt = 1, maxInt = 5) Integer> items) {

            assertThat(
                items.size(),
                allOf(greaterThanOrEqualTo(4), lessThanOrEqualTo(5)));
        }
    }

    @Test public void shrinkingDistinctSets() {
        assertThat(testResult(ShrinkingDistinctSets.class), failureCountIs(1));
        assertThat(
            ShrinkingDistinctSets.failed.size(),
            allOf(greaterThanOrEqualTo(4), lessThanOrEqualTo(5)));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingDistinctSets {
        static Set<Integer> failed;

        @Property public void shouldHold(
            @Size(min = 4, max = 5)
            @Distinct
                Set<@InRange(minInt = 1, maxInt = 5) Integer> items) {

            failed = items;

            fail();
        }
    }
}

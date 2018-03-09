package com.pholser.junit.quickcheck;

import com.pholser.junit.quickcheck.generator.Distinct;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class DistinctMapPropertyParameterTypesTest {
    @Test public void distinctMaps() {
        assertThat(testResult(DistinctMaps.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class DistinctMaps {
        @Property public void shouldHold(
            @Size(min = 4, max = 5) @Distinct Map<@InRange(minInt = 1, maxInt = 5) Integer, Double> items) {
            assertThat(items.size(), allOf(greaterThanOrEqualTo(4), lessThanOrEqualTo(5)));
        }
    }

    @Test public void shrinkingDistinctMaps() {
        assertThat(testResult(ShrinkingDistinctMaps.class), failureCountIs(1));
        assertThat(
            ShrinkingDistinctMaps.failed.size(),
            allOf(greaterThanOrEqualTo(4), lessThanOrEqualTo(5)));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingDistinctMaps {
        static Map<Integer, Double> failed;

        @Property public void shouldHold(
            @Size(min = 4, max = 5) @Distinct Map<@InRange(minInt = 1, maxInt = 5) Integer, Double> items) {
            failed = items;

            fail();
        }
    }
}

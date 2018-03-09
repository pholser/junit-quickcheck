package com.pholser.junit.quickcheck;

import com.pholser.junit.quickcheck.generator.Distinct;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.internal.Lists;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class DistinctListPropertyParameterTypesTest {
    @Test public void distinctLists() {
        assertThat(testResult(DistinctLists.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class DistinctLists {
        @Property public void shouldHold(
            @Size(min = 2, max = 5) @Distinct List<@InRange(minInt = 1, maxInt = 5) Integer> items) {
            assertThat(items.size(), allOf(greaterThanOrEqualTo(2), lessThanOrEqualTo(5)));
            assertThat(Lists.isDistinct(items), is(true));
        }
    }

    @Test public void shrinkingDistinctLists() {
        assertThat(testResult(ShrinkingDistinctLists.class), failureCountIs(1));
        assertThat(
            ShrinkingDistinctLists.failed.size(),
            allOf(greaterThanOrEqualTo(4), lessThanOrEqualTo(5)));
        assertThat(Lists.isDistinct(ShrinkingDistinctLists.failed), is(true));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingDistinctLists {
        static List<Integer> failed;

        @Property public void shouldHold(
            @Size(min = 4, max = 5) @Distinct List<@InRange(minInt = 1, maxInt = 5) Integer> items) {
            failed = items;

            fail();
        }
    }
}

package com.pholser.junit.quickcheck.internal;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.google.common.collect.Lists.*;
import static java.util.Collections.*;
import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.*;

public class ListsTest {
    @Rule public final ExpectedException thrown = none();

    @Test public void rejectsNegativeRemovalCount() {
        thrown.expect(IllegalArgumentException.class);

        Lists.removeFrom(newArrayList("abc"), -1);
    }

    @Test public void removalsOfZeroElementsFromAList() {
        List<Integer> target = newArrayList(1, 2, 3);

        assertEquals(singletonList(target), Lists.removeFrom(target, 0));
    }
}

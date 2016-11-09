package com.pholser.junit.quickcheck;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

public interface ComparableWithConsistentEqualsContract<T extends Comparable<T>> {
    @Property default void equalsConsistency(T thing) {
        T other = thingComparableTo(thing);
        assumeThat(thing.compareTo(other), equalTo(0));

        assertEquals(thing, other);
    }

    T thingComparableTo(T thing);
}
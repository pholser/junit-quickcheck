package com.pholser.junit.quickcheck;

import java.util.Comparator;

import static java.lang.Math.*;
import static org.junit.Assert.*;

public interface ComparatorContract<T> {
    Comparator<T> getComparator();

    @Property default void symmetry(T x, T y) {
        Comparator<T> comparator = getComparator();

        assertEquals(
            signum(comparator.compare(x, y)),
            -signum(comparator.compare(y, x)),
            0F);
    }
}

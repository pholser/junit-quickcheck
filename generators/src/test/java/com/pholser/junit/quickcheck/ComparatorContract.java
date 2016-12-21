package com.pholser.junit.quickcheck;

import java.util.Comparator;

import static java.lang.Math.*;
import static org.junit.Assert.*;

public interface ComparatorContract<T> {
    Comparator<T> subject();

    @Property default void symmetry(T x, T y) {
        Comparator<T> subject = subject();

        assertEquals(
            signum(subject.compare(x, y)),
            -signum(subject.compare(y, x)),
            0F);
    }
}

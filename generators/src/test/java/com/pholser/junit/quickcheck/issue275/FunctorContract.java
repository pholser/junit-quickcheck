package com.pholser.junit.quickcheck.issue275;

import static org.junit.Assert.assertEquals;

import com.pholser.junit.quickcheck.Property;
import java.util.function.Function;

public interface FunctorContract<F> {
    Functor<F> subject();

    @Property
    default void mapIdentity(__<F, String> a) {
        Functor<F> functor = subject();

        __<F, String> mappedA = functor.map(Function.identity(), a);

        assertEquals(a, mappedA);
    }
}

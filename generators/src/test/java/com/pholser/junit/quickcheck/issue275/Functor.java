package com.pholser.junit.quickcheck.issue275;

import java.util.function.Function;

public interface Functor<F> {
    <A, B> __<F, B> map(Function<A, B> fn, __<F, A> nestedA);
}

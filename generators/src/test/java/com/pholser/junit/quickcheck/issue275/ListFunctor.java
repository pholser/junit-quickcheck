package com.pholser.junit.quickcheck.issue275;

import java.util.function.Function;

public class ListFunctor implements Functor<List.mu> {
    @Override
    @SuppressWarnings("unchecked")
    public <A, B> __<List.mu, B> map(
        Function<A, B> fn, __<List.mu, A> nestedA) {

        return (__<List.mu, B>) nestedA;
    }
}

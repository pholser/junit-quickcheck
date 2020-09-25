package com.pholser.junit.quickcheck.issue275;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

@RunWith(JUnitQuickcheck.class)
public class ListFunctorProperties implements FunctorContract<List.mu> {
    @Override
    public ListFunctor subject() {
        return new ListFunctor();
    }
}

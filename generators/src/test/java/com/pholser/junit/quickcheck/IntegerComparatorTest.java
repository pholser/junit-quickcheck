package com.pholser.junit.quickcheck;

import java.util.Comparator;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

@RunWith(JUnitQuickcheck.class)
public class IntegerComparatorTest<T> implements ComparatorContract<Integer> {
    @Override public Comparator<Integer> subject() {
        return Integer::compare;
    }
}

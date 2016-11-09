package com.pholser.junit.quickcheck;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

@RunWith(JUnitQuickcheck.class)
public class IntegerComparableTest
    implements ComparableWithConsistentEqualsContract<Integer> {

    @Override public Integer thingComparableTo(Integer thing) {
        return thing;
    }
}

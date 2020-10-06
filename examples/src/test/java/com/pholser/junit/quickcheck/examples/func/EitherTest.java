package com.pholser.junit.quickcheck.examples.func;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

@RunWith(JUnitQuickcheck.class)
public class EitherTest {
    @Property
    public void withRanges(
        Either<
            @InRange(minInt = 0) Integer,
            @InRange(minDouble = -7.0, maxDouble = -4.0) Double> e) {

        e.map(
            left -> {
                assertThat(left, greaterThanOrEqualTo(0));
                return 0;
            },
            right -> {
                assertThat(
                    right,
                    allOf(
                        greaterThanOrEqualTo(-7.0),
                        lessThanOrEqualTo(-4.0)));
                return 1;
            }
        );
    }
}

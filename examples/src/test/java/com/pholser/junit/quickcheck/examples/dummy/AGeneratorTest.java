package com.pholser.junit.quickcheck.examples.dummy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

@RunWith(JUnitQuickcheck.class)
public class AGeneratorTest {
    @Property public void listAreCorrectlyGenerated(A a) {
        a.getListOfB().forEach(b -> assertThat(b, instanceOf(B.class)));
    }
}

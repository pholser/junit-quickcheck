package com.pholser.junit.quickcheck;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.Box;
import com.pholser.junit.quickcheck.test.generator.Foo;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

public class PropertyRuleTest {
    @Test public void withPropertyRule() {

    }

    @RunWith(JUnitQuickcheck.class)
    public static class WithPropertyRule {
        private Foo f;

        @Rule public final PropertyRule p = new PropertyRule() {
            public void prime(Foo f) {
                System.out.printf("prime %s\n", f);
                WithPropertyRule.this.f = f;
            }
        };

        @Property(trials = 4) public void holds(Box<Foo> b) {
            System.out.printf("%s %s\n", f, b);
        }
    }
}

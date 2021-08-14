/*
 The MIT License

 Copyright (c) 2010-2021 Paul R. Holser, Jr.

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.pholser.junit.quickcheck;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeThat;
import static org.junit.Assume.assumeTrue;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.failureCountIs;
import static org.junit.experimental.results.ResultMatchers.hasSingleFailureContaining;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.AFooBadShrinks;
import com.pholser.junit.quickcheck.test.generator.Between;
import com.pholser.junit.quickcheck.test.generator.Foo;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

public class ShrinkingTest {
    @Test public void complete() {
        assertThat(
            testResult(ShrinkingCompletely.class),
            hasSingleFailureContaining(
                String.format("With arguments: [%s]", new Foo(1))));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingCompletely {
        @Property(
            maxShrinks = Integer.MAX_VALUE,
            maxShrinkDepth = Integer.MAX_VALUE)
        public void shouldHold(Foo f) {
            assumeThat(f.i(), greaterThan(0));

            assertThat(f.i(), lessThan(1));
        }
    }

    @Test public void shrinkingDoesNotShrink() {
        assertThat(
            testResult(ShrinkingNotReally.class),
            hasSingleFailureContaining("With arguments: ["));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingNotReally {
        @Property public void shouldHold(Foo f) {
            assumeThat(f.i(), greaterThan(Integer.MAX_VALUE / 2));

            assertThat(f.i(), lessThan(Integer.MAX_VALUE / 2));
        }
    }

    @Test public void shrinkingDoesNotShrinkWhenLargerEqualsSmaller() {
        assertThat(
            testResult(ShrinksAreIdentity.class),
            hasSingleFailureContaining("With arguments: ["));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinksAreIdentity {
        @Property public void shouldHold(@From(AFooBadShrinks.class) Foo f) {
            fail();
        }
    }

    @Test public void assumptionFailureWhileShrinking() {
        assertThat(
            testResult(FailedAssumptionDuringShrinking.class),
            hasSingleFailureContaining("With arguments: ["));
        FailedAssumptionDuringShrinking.shrinking = false;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class FailedAssumptionDuringShrinking {
        private static boolean shrinking;

        @Property public void shouldHold(Foo f) {
            if (!shrinking) {
                shrinking = true;
                fail();
            }

            assumeFalse(shrinking);
        }
    }

    @Test public void disablingShrinking() {
        assertThat(testResult(DisablingShrinking.class), failureCountIs(1));
        assertEquals(1, DisablingShrinking.attempts.size());
        DisablingShrinking.attempts.clear();
    }

    @RunWith(JUnitQuickcheck.class)
    public static class DisablingShrinking {
        static final List<Foo> attempts = new ArrayList<>();

        @Property(shrink = false) public void shouldHold(Foo f) {
            attempts.add(f);

            fail();
        }
    }

    @Test public void shrinkingArray() {
        assertThat(testResult(ShrinkingArray.class), failureCountIs(1));
        assertThat(ShrinkingArray.attempts.size(), greaterThan(0));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingArray {
        static final List<Foo[]> attempts = new ArrayList<>();

        @Property public void shouldHold(Foo[] f) {
            assumeThat(f.length, greaterThan(0));

            attempts.add(f);

            fail();
        }
    }

    @Test public void shrinkingLengthConstrainedArray() {
        assertThat(
            testResult(ShrinkingLengthConstrainedArray.class),
            failureCountIs(1));
        assertTrue(
            ShrinkingLengthConstrainedArray.attempts.stream()
                .allMatch(a -> a.length >= 2 && a.length <= 4));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingLengthConstrainedArray {
        static final List<Foo[]> attempts = new ArrayList<>();

        @Property public void shouldHold(Foo @Size(min = 2, max = 4) [] f) {
            attempts.add(f);

            fail();
        }
    }

    @Test public void assumptionsNeverMet() {
        assertThat(testResult(AssumptionsNeverMet.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class AssumptionsNeverMet {
        @Property public void shouldHold(Foo f) {
            assumeTrue(false);
        }
    }

    @Test public void unexpectedErrorInProperty() {
        assertThat(
            testResult(UnexpectedErrorInProperty.class),
            hasSingleFailureContaining(
                "Unexpected error in property shouldHold with args ["));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class UnexpectedErrorInProperty {
        @Property public void shouldHold(Foo f) {
            throw new IllegalStateException();
        }
    }

    @Test public void unexpectedErrorDuringShrinking() {
        assertThat(
            testResult(UnexpectedErrorDuringShrinking.class),
            hasSingleFailureContaining(
                "Unexpected error in property shouldHold with args ["));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class UnexpectedErrorDuringShrinking {
        private static boolean shrinking;

        @Property public void shouldHold(Foo f) {
            if (shrinking)
                throw new IllegalStateException();

            shrinking = true;
            fail();
        }
    }

    @Test public void shrinkingMoreThanOnePropertyParameter() {
        assertThat(
            testResult(ShrinkingMoreThanOnePropertyParameter.class),
            hasSingleFailureContaining(
                String.format(
                    "With arguments: [%s, %s]",
                    new Foo(1),
                    new Foo(1))));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingMoreThanOnePropertyParameter {
        @Property public void shouldHold(
            @Between(min = 1, max = 500) Foo first,
            @Between(min = 1, max = 500) Foo second) {

            assertThat(first.i(), lessThan(1));
            assertThat(second.i(), lessThan(1));
        }
    }

    @Test public void timeout() {
        assertThat(
            testResult(ShrinkingTimeout.class),
            hasSingleFailureContaining("With arguments: ["));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingTimeout {
        @Property(
            maxShrinks = Integer.MAX_VALUE,
            maxShrinkDepth = Integer.MAX_VALUE,
            maxShrinkTime = 200)
        public void shouldHold(Foo f) throws InterruptedException {
            assumeThat(f.slow(), greaterThan(Integer.MAX_VALUE / 2));
            assertThat(f.slow(), lessThan(Integer.MAX_VALUE / 2));
        }
    }

    @Test public void shrinkingInPresenceOfConstraintExpression() {
        assertThat(
            testResult(ShrinkingInPresenceOfConstraintExpression.class),
            failureCountIs(1));
        assertTrue(
            ShrinkingInPresenceOfConstraintExpression.values
                .stream()
                .allMatch(f -> f.i() >= 0));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingInPresenceOfConstraintExpression {
        private static final List<Foo> values = new ArrayList<>();

        @Property public void shouldHold(
            @When(satisfies = "#_.i >= 0") Foo f) {
            values.add(f);

            fail();
        }
    }
}

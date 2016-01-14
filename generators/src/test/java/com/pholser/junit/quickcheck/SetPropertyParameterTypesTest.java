/*
 The MIT License

 Copyright (c) 2010-2016 Paul R. Holser, Jr.

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

import java.util.Set;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Classes.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class SetPropertyParameterTypesTest {
    @Test public void huh() {
        assertThat(testResult(SetOfHuh.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class SetOfHuh {
        @Property public void shouldHold(Set<?> items) {
        }
    }

    @Test public void lowerBounded() {
        assertThat(testResult(SetOfLowerBound.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class SetOfLowerBound {
        @Property(trials = 15) public void shouldHold(Set<? extends Integer> items) {
            if (!items.isEmpty()) {
                assertThat(
                    Integer.class,
                    isAssignableFrom(nearestCommonSuperclassOf(classesOf(items))));
            }
        }
    }

    @Test public void upperBounded() {
        assertThat(testResult(SetOfUpperBound.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class SetOfUpperBound {
        @Property public void shouldHold(Set<? super String> items) {
        }
    }

    @Test public void shrinkingUpperBounded() {
        assertThat(testResult(ShrinkingSetOfUpperBounded.class), failureCountIs(1));
        assertThat(ShrinkingSetOfUpperBounded.failed.size(), greaterThan(0));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingSetOfUpperBounded {
        static Set<? super String> failed;

        @Property public void shouldHold(Set<? super String> items) {
            assumeFalse(items.isEmpty());

            failed = items;
            fail();
        }
    }

    @Test public void intArray() {
        assertThat(testResult(SetOfIntArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class SetOfIntArray {
        @Property public void shouldHold(Set<int[]> items) {
            for (int[] each : items) {
                // ensuring the cast works
            }
        }
    }

    @Test public void setOfHuh() {
        assertThat(testResult(SetOfSetOfHuh.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class SetOfSetOfHuh {
        @Property(trials = 10) public void shouldHold(Set<Set<?>> items) {
            for (Set<?> each : items) {
                // ensuring the cast works
            }
        }
    }

    @Test public void setOfInteger() {
        assertThat(testResult(SetOfSetOfInteger.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class SetOfSetOfInteger {
        @Property public void shouldHold(Set<Set<Integer>> items) {
            for (Set<Integer> each : items) {
                // ensuring the cast works
            }
        }
    }

    @Test public void setOfUpperBounded() {
        assertThat(testResult(SetOfSetOfUpperBound.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class SetOfSetOfUpperBound {
        @Property public void shouldHold(Set<Set<? extends Number>> items) {
            for (Set<? extends Number> each : items) {
                if (!items.isEmpty()) {
                    assertThat(
                        Number.class,
                        isAssignableFrom(nearestCommonSuperclassOf(classesOf(each))));
                }
            }
        }
    }

    @Test public void setOfLowerBounded() {
        assertThat(testResult(SetOfSetOfLowerBound.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class SetOfSetOfLowerBound {
        @Property(trials = 10) public void shouldHold(Set<Set<? super Float>> items) {
            for (Set<? super Float> each : items) {
            }
        }
    }
}

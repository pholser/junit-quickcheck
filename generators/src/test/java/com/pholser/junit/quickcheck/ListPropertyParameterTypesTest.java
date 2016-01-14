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

import java.util.List;

import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Classes.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ListPropertyParameterTypesTest {
    @Test public void huh() {
        assertThat(testResult(ListOfHuh.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ListOfHuh {
        @Property public void shouldHold(List<?> items) {
        }
    }

    @Test public void shrinkingHuh() {
        assertThat(testResult(ShrinkingListOfHuh.class), failureCountIs(1));
        assertThat(ShrinkingListOfHuh.failed.size(), greaterThan(0));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingListOfHuh {
        static List<?> failed;

        @Property public void shouldHold(List<?> items) {
            assumeFalse(items.isEmpty());

            failed = items;
            fail();
        }
    }

    @Test public void upperBounded() {
        assertThat(testResult(ListOfUpperBound.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ListOfUpperBound {
        @Property public void shouldHold(List<? extends Integer> items) {
            if (!items.isEmpty()) {
                assertThat(
                    Integer.class,
                    isAssignableFrom(nearestCommonSuperclassOf(classesOf(items))));
            }
        }
    }

    @Test public void lowerBounded() {
        assertThat(testResult(ListOfLowerBound.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ListOfLowerBound {
        @Property(trials = 10) public void shouldHold(List<? super Number> items) {
        }
    }

    @Test public void intArray() {
        assertThat(testResult(ListOfIntArray.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ListOfIntArray {
        @Property public void shouldHold(List<int[]> items) {
            for (int[] each : items) {
                for (int i : each) {
                    // ensuring the cast works
                }
            }
        }
    }

    @Test public void listOfHuh() {
        assertThat(testResult(ListOfListOfHuh.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ListOfListOfHuh {
        @Property(trials = 10) public void shouldHold(List<List<?>> items) {
            for (List<?> each : items) {
                // ensuring the cast works
            }
        }
    }

    @Test public void listOfInteger() {
        assertThat(testResult(ListOfListOfInteger.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ListOfListOfInteger {
        @Property public void shouldHold(List<List<Integer>> items) {
            for (List<Integer> each : items) {
                for (Integer i : each) {
                    // ensuring the cast works
                }
            }
        }
    }

    @Test public void listOfRangedInteger() {
        assertThat(testResult(ListOfListOfRangedInteger.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ListOfListOfRangedInteger {
        @Property public void shouldHold(List<List<@InRange(min = "0", max = "9") Integer>> items) {
            for (List<Integer> each : items) {
                for (Integer i : each)
                    assertThat(i, allOf(greaterThanOrEqualTo(0), lessThanOrEqualTo(9)));
            }
        }
    }

    @Test public void shrinkingListOfListRangedInteger() {
        assertThat(testResult(ShrinkingListOfListOfRangedInteger.class), failureCountIs(1));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingListOfListOfRangedInteger {
        @Property public void shouldHold(List<List<@InRange(min = "0", max = "9") Integer>> items) {
            assumeFalse(items.isEmpty());

            for (List<Integer> each : items) {
                assumeFalse(each.isEmpty());
                for (Integer i : each)
                    assertThat(i, allOf(greaterThanOrEqualTo(0), lessThanOrEqualTo(9)));
            }

            fail();
        }
    }

    @Test public void listOfUpperBounded() {
        assertThat(testResult(ListOfListOfUpperBound.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ListOfListOfUpperBound {
        @Property(trials = 7) public void shouldHold(List<List<? extends Number>> items) {
            for (List<? extends Number> each : items) {
                if (!each.isEmpty()) {
                    assertThat(
                        Number.class,
                        isAssignableFrom(nearestCommonSuperclassOf(classesOf(each))));
                }
            }
        }
    }

    @Test public void listOfLowerBounded() {
        assertThat(testResult(ListOfListOfLowerBound.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ListOfListOfLowerBound {
        @Property(trials = 5) public void shouldHold(List<List<? super Float>> items) {
        }
    }

    @Test public void listOfVoid() throws Exception {
        assertThat(testResult(ListOfVoid.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ListOfVoid {
        @Property public void shouldHold(List<Void> voids) {
            for (Void each : voids)
                assertNull(each);
        }
    }

    @Test public void doesNotGetStuckWithConstraintExpression() {
        assertThat(testResult(UsedToGetStuckOnConstraintExpression.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class UsedToGetStuckOnConstraintExpression {
        @Property(trials = 8)
        public void myTest(@When(satisfies = "not #_.empty") List<Integer> ints) {
            assertThat(ints.size(), greaterThan(0));
        }
    }

    @Test public void listsOfNumbers() {
        assertThat(testResult(NumberProperties.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class NumberProperties<N extends Number> {
        @Property public void holds(List<@InRange(min = "0", max = "100") N> numbers) {
            for (Number each : numbers)
                assertThat(each.intValue(), allOf(greaterThanOrEqualTo(0), lessThanOrEqualTo(100)));
        }
    }

    @Test public void listsOfIntegers() {
        assertThat(testResult(IntegerProperties.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class IntegerProperties extends NumberProperties<Integer> {
    }
}

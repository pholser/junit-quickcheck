/*
 The MIT License

 Copyright (c) 2010-2013 Paul R. Holser, Jr.

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

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Classes.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ForAllSetTheoryParameterTypesTest {
    @Test public void huh() {
        assertThat(testResult(SetOfHuh.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class SetOfHuh {
        @Theory public void shouldHold(@ForAll Set<?> items) {
        }
    }

    @Test public void lowerBounded() {
        assertThat(testResult(SetOfLowerBound.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class SetOfLowerBound {
        @Theory public void shouldHold(@ForAll(sampleSize = 15) Set<? extends Integer> items) {
            if (!items.isEmpty()) {
                Class<?> superclass = nearestCommonSuperclassOf(classesOf(items));
                assertThat(Integer.class, isAssignableFrom(superclass));
            }
        }
    }

    @Test public void upperBounded() {
        assertThat(testResult(SetOfUpperBound.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class SetOfUpperBound {
        @Theory public void shouldHold(@ForAll Set<? super String> items) {
        }
    }

    @Test public void intArray() {
        assertThat(testResult(SetOfIntArray.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class SetOfIntArray {
        @Theory public void shouldHold(@ForAll Set<int[]> items) {
            for (int[] each : items) {
                // ensuring the cast works
            }
        }
    }

    @Test public void setOfHuh() {
        assertThat(testResult(SetOfSetOfHuh.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class SetOfSetOfHuh {
        @Theory public void shouldHold(@ForAll(sampleSize = 10) Set<Set<?>> items) {
            for (Set<?> each : items) {
                // ensuring the cast works
            }
        }
    }

    @Test public void setOfInteger() {
        assertThat(testResult(SetOfSetOfInteger.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class SetOfSetOfInteger {
        @Theory public void shouldHold(@ForAll Set<Set<Integer>> items) {
            for (Set<Integer> each : items) {
                // ensuring the cast works
            }
        }
    }

    @Test public void setOfUpperBounded() {
        assertThat(testResult(SetOfSetOfUpperBound.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class SetOfSetOfUpperBound {
        @Theory public void shouldHold(@ForAll Set<Set<? extends Number>> items) {
            for (Set<? extends Number> each : items) {
                if (!items.isEmpty()) {
                    Class<?> superclass = nearestCommonSuperclassOf(classesOf(each));
                    assertThat(Number.class, isAssignableFrom(superclass));
                }
            }
        }
    }

    @Test public void setOfLowerBounded() {
        assertThat(testResult(SetOfSetOfLowerBound.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class SetOfSetOfLowerBound {
        @Theory public void shouldHold(@ForAll(sampleSize = 10) Set<Set<? super Float>> items) {
            for (Set<? super Float> each : items) {
            }
        }
    }
}

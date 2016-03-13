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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.Foo;

public class OnFailingSetHookTest {

    public static class StoreFailingSetInGlobalVariableHook implements OnFailingSetHook {
        static Object[] counterExample = null;
        static int counter = 0;

        static void reset() {
            counterExample = null;
            counter = 0;
        }

        @Override
        public void handle(Object[] counterExample, Runnable repeatTestOption) {
            this.counterExample = counterExample;
            this.counter++;
        }
    }

    @Before public void resetStoreFailingSetInGlobalVariableHook() {
        StoreFailingSetInGlobalVariableHook.reset();
    }

    @Test public void shouldNotCallOnFailingSetHookIfTestSucceeds() throws Exception {
        assumeThat(
                testResult(SuccessfulTest.class),
                isSuccessful());

        assertEquals(0, StoreFailingSetInGlobalVariableHook.counter);
        assertNull(StoreFailingSetInGlobalVariableHook.counterExample);
    }

    @RunWith(JUnitQuickcheck.class)
    public static class SuccessfulTest {
        @Property(onFailingSet = StoreFailingSetInGlobalVariableHook.class)
        public void shouldHold(Foo f) {
        }
    }

    @Test public void onFailingSetHookShouldBeCalled() throws Exception {
        assumeThat(
                testResult(FailingTestWithShrinking.class),
                not(isSuccessful()));

        assertEquals(1, StoreFailingSetInGlobalVariableHook.counter);
        assertNotNull(StoreFailingSetInGlobalVariableHook.counterExample);
        assertThat(StoreFailingSetInGlobalVariableHook.counterExample, instanceOf(Object[].class));
        assertEquals(1, StoreFailingSetInGlobalVariableHook.counterExample.length);
        assertThat(StoreFailingSetInGlobalVariableHook.counterExample[0], instanceOf(Foo.class));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class FailingTestWithShrinking {
        @Property(onFailingSet = StoreFailingSetInGlobalVariableHook.class)
        public void shouldHold(Foo f) {
            assumeThat(f.i(), greaterThan(Integer.MAX_VALUE / 2));
            assertThat(f.i(), lessThan(Integer.MAX_VALUE / 2));
        }
    }

    @Test public void onFailingSetHookShouldBeCalledForFailingTestsWithoutShrinking() throws Exception {
        assumeThat(
                testResult(FailingTestWithShrinkingDisabled.class),
                not(isSuccessful()));

        assertEquals(1, StoreFailingSetInGlobalVariableHook.counter);
        assertNotNull(StoreFailingSetInGlobalVariableHook.counterExample);
        assertThat(StoreFailingSetInGlobalVariableHook.counterExample, instanceOf(Object[].class));
        assertEquals(1, StoreFailingSetInGlobalVariableHook.counterExample.length);
        assertThat(StoreFailingSetInGlobalVariableHook.counterExample[0], instanceOf(Foo.class));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class FailingTestWithShrinkingDisabled {
        @Property(onFailingSet = StoreFailingSetInGlobalVariableHook.class, shrink = false)
        public void shouldHold(Foo f) {
            assumeThat(f.i(), greaterThan(Integer.MAX_VALUE / 2));
            assertThat(f.i(), lessThan(Integer.MAX_VALUE / 2));
        }
    }

    @Test public void shouldBeAbleToRepeatFailedTestsWithShrinkingEnabled() throws Exception {
        assumeThat(FailingTestWhichIsRepeatedThreeTimesAfterFailure.calls, iterableWithSize(0));
        assumeThat(
                testResult(FailingTestWhichIsRepeatedThreeTimesAfterFailure.class),
                not(isSuccessful()));

        List<Integer> calls = FailingTestWhichIsRepeatedThreeTimesAfterFailure.calls;
        assertThat(calls.size(), greaterThanOrEqualTo(3));

        // at least the last three calls should be equal
        assertEquals(calls.get(calls.size() - 1), calls.get(calls.size() - 2));
        assertEquals(calls.get(calls.size() - 1), calls.get(calls.size() - 3));
    }

    public static class RepeatFailingTestThreeTimes implements OnFailingSetHook {

        @Override
        public void handle(Object[] counterExample, Runnable repeatTestOption) {
            for (int i = 0; i < 3; i++) {
                try {
                    repeatTestOption.run();
                } catch (Exception | AssertionError e) {}
            }
        }
    }

    @RunWith(JUnitQuickcheck.class)
    public static class FailingTestWhichIsRepeatedThreeTimesAfterFailure {

        static List<Integer> calls = new ArrayList<>();

        static void reset() {
            calls = new ArrayList<>();
        }

        @Property(onFailingSet = RepeatFailingTestThreeTimes.class)
        public void shouldHold(Foo f) {
            calls.add(f.i());
            fail();
        }
    }

    @Before public void resetFailingTestWhichIsRepeatedThreeTimesAfterFailure() {
        FailingTestWhichIsRepeatedThreeTimesAfterFailure.reset();
    }

    @Test public void shouldBeAbleToRepeatFailedTestsWithShrinkingDisabled() throws Exception {
        assumeThat(FailingTestWhichIsRepeatedThreeTimesAfterFailureWithoutShrinking.calls, iterableWithSize(0));
        assumeThat(
                testResult(FailingTestWhichIsRepeatedThreeTimesAfterFailureWithoutShrinking.class),
                not(isSuccessful()));

        // should result in four identical calls (the first try immediately fails, followed by three repetitions)
        List<Integer> calls = FailingTestWhichIsRepeatedThreeTimesAfterFailureWithoutShrinking.calls;
        assertEquals(4, calls.size());
        assertEquals(1, new HashSet<>(calls).size());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class FailingTestWhichIsRepeatedThreeTimesAfterFailureWithoutShrinking {

        static List<Integer> calls = new ArrayList<>();

        static void reset() {
            calls = new ArrayList<>();
        }

        @Property(onFailingSet = RepeatFailingTestThreeTimes.class, shrink = false)
        public void shouldHold(Foo f) {
            calls.add(f.i());
            fail();
        }
    }

    @Before public void resetFailingTestWhichIsRepeatedThreeTimesAfterFailureWithoutShrinking() {
        FailingTestWhichIsRepeatedThreeTimesAfterFailureWithoutShrinking.reset();
    }

}

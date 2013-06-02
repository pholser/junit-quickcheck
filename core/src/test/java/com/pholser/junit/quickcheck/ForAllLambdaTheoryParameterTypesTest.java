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

import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.Callable;

import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.IntegerGenerator;
import com.pholser.junit.quickcheck.generator.LongGenerator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Annotations.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ForAllLambdaTheoryParameterTypesTest {
    @Test public void runnable() throws Exception {
        assertThat(testResult(RunnableParameter.class), isSuccessful());
        assertEquals(defaultSampleSize(), RunnableParameter.iterations);
    }

    @RunWith(Theories.class)
    public static class RunnableParameter {
        static int iterations;

        @Theory public void shouldHold(@ForAll Runnable task) {
            ++iterations;
        }
    }

    @Test public void callable() throws Exception {
        assertThat(testResult(CallableParameter.class), isSuccessful());
        assertEquals(defaultSampleSize(), CallableParameter.iterations);
    }

    @RunWith(Theories.class)
    public static class CallableParameter {
        static int iterations;

        @Theory public void shouldHold(@ForAll Callable<? extends Long> task) throws Exception {
            ++iterations;

            Long value = functionValue(new LongGenerator(), null);
            for (int i = 0; i < 10000; ++i)
                assertEquals(value, task.call());
        }
    }

    @Test public void actionListener() throws Exception {
        assertThat(testResult(ActionListenerParameter.class), isSuccessful());
        assertEquals(defaultSampleSize(), ActionListenerParameter.iterations);
    }

    @RunWith(Theories.class)
    public static class ActionListenerParameter {
        static int iterations;

        @Theory public void shouldHold(@ForAll ActionListener listener) {
            ++iterations;
        }
    }

    @Test public void comparatorOfString() throws Exception {
        assertThat(testResult(ComparatorOfString.class), isSuccessful());
        assertEquals(defaultSampleSize(), ComparatorOfString.iterations);
    }

    @RunWith(Theories.class)
    public static class ComparatorOfString {
        static int iterations;

        @Theory public void shouldHold(@ForAll Comparator<String> c) {
            ++iterations;

            int value = functionValue(new IntegerGenerator(), new Object[] { "foo", "bar" });
            for (int i = 0; i < 10000; ++i)
                assertEquals(value, c.compare("foo", "bar"));
        }
    }

    private static <T> T functionValue(Generator<T> generator, Object[] args) {
        Random r = new Random();
        r.setSeed(java.util.Arrays.hashCode(args));
        return generator.generate(new SourceOfRandomness(r), null);
    }
}

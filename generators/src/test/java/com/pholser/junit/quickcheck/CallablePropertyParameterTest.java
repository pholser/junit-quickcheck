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

import java.util.concurrent.Callable;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Annotations.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class CallablePropertyParameterTest {
    @Test public void callable() throws Exception {
        assertThat(testResult(CallableOfInt.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), CallableOfInt.iterations);
        CallableOfInt.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class CallableOfInt {
        static int iterations;

        @Property public void shouldHold(
            Callable<Integer> c1,
            Callable<Integer> c2)
            throws Exception {

            ++iterations;

            Integer v1 = c1.call();
            Integer v2 = c2.call();
            for (int i = 0; i < 10000; ++i) {
                assertEquals(v1, c1.call());
                assertEquals(v2, c2.call());
                assertNotEquals(v1, v2);
            }
        }
    }
}

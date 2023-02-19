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

import static com.pholser.junit.quickcheck.Annotations.defaultPropertyTrialCount;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.Box;
import com.pholser.junit.quickcheck.test.generator.Foo;
import com.pholser.junit.quickcheck.test.generator.FooBoxOpener;
import org.junit.Test;
import org.junit.runner.RunWith;

public class LambdaPropertyParameterTypesTest {
    @Test public void unboxingAFoo() throws Exception {
        assertThat(testResult(UnboxingAFoo.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), UnboxingAFoo.iterations);
        UnboxingAFoo.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class UnboxingAFoo {
        static int iterations;

        @Property public void shouldHold(FooBoxOpener b1, FooBoxOpener b2) {
            ++iterations;

            Foo f1 = b1.open(new Box<>(new Foo(2)));
            Foo f2 = b2.open(new Box<>(new Foo(4)));
            for (int i = 0; i < 10000; ++i) {
                assertEquals(f1, b1.open(new Box<>(new Foo(2))));
                assertEquals(f2, b2.open(new Box<>(new Foo(4))));
                assertNotEquals(f1, f2);
            }
        }
    }
}

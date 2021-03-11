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

package com.pholser.junit.quickcheck.generator.java.util.function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import java.util.function.BiConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;

public class BiConsumerPropertyParameterTest {
    @Test public void check() {
        assertThat(testResult(AnyOldConsumer.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class AnyOldConsumer<T, U> {
        @Property public void anyOld(
            BiConsumer<? super T, ? super U> consumer,
            T first,
            U second) {

            consumer.accept(first, second);
        }

        @Property public void chained(
            BiConsumer<T, U> first,
            BiConsumer<? super T, ? super U> second,
            T firstArg,
            U secondArg) {

            first.andThen(second).accept(firstArg, secondArg);
        }
    }
}

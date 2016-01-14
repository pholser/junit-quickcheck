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

package com.pholser.junit.quickcheck.generator.java.util.function;

import java.util.function.BiPredicate;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class BiPredicatePropertyParameterTest {
    @Test public void definiteArgType() {
        assertThat(testResult(DefiniteArgType.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class DefiniteArgType {
        @Property public void x(BiPredicate<String, Integer> p) {
            p.test("abc", 4);
        }
    }

    @Test public void callingDefaultPredicateMethod() {
        assertThat(testResult(CallingDefaultPredicateMethod.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class CallingDefaultPredicateMethod {
        @Property public <T, U> void or(
            BiPredicate<T, U> first,
            BiPredicate<? super T, ? super U> second,
            T firstArg,
            U secondArg) {

            boolean firstResult = first.test(firstArg, secondArg);
            boolean secondResult = second.test(firstArg, secondArg);

            assertEquals(firstResult || secondResult, first.or(second).test(firstArg, secondArg));
        }

        @Property public <T, U> void negate(BiPredicate<T, U> p, T first, U second) {
            boolean result = p.test(first, second);

            assertEquals(!result, p.negate().test(first, second));
        }
    }
}

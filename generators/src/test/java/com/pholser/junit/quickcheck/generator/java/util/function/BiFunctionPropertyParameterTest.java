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

import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class BiFunctionPropertyParameterTest {
    @Test public void unresolvedTypes() {
        assertThat(testResult(Unresolved.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Unresolved<T, U, R> {
        @Property public void typesAreOk(
            BiFunction<? super T, ? super U, ? extends R> f,
            T first,
            U second) {

            R result = f.apply(first, second);
        }
    }

    @Test public void unresolvedFirstArgType() {
        assertThat(testResult(UnresolvedFirstArgType.class), isSuccessful());
    }

    public static class UnresolvedFirstArgType<T> extends Unresolved<T, Boolean, Integer> {
        @Property public void consistent(
            BiFunction<? super T, ? super Boolean, Integer> f,
            T first,
            boolean second) {

            Integer result = f.apply(first, second);

            for (int i = 0; i < 10000; ++i)
                assertEquals(result, f.apply(first, second));
        }
    }

    @Test public void unresolvedSecondArgType() {
        assertThat(testResult(UnresolvedSecondArgType.class), isSuccessful());
    }

    public static class UnresolvedSecondArgType<U> extends Unresolved<Boolean, U, Short> {
        @Property public void consistent(
            BiFunction<? super Boolean, ? super U, Short> f,
            boolean first,
            U second) {

            Short result = f.apply(first, second);

            for (int i = 0; i < 10000; ++i)
                assertEquals(result, f.apply(first, second));
        }
    }

    @Test public void resolvedTypes() {
        assertThat(testResult(ResolvedTypes.class), isSuccessful());
    }

    public static class ResolvedTypes extends Unresolved<Boolean, Integer, Date> {
    }

    @Test public void callingDefaultFunctionMethod() {
        assertThat(testResult(CallingDefaultFunctionMethod.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class CallingDefaultFunctionMethod {
        @Property public void defaultMethods(
            BiFunction<Boolean, Integer, Date> first,
            Function<? super Date, Double> second,
            boolean firstArg,
            int secondArg) {

            Date intermediate = first.apply(firstArg, secondArg);
            Double ultimate = second.apply(intermediate);

            assertEquals(ultimate, first.andThen(second).apply(firstArg, secondArg));
        }
    }
}

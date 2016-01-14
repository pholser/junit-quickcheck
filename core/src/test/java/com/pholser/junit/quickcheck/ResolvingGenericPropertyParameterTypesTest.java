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

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.Foo;
import com.pholser.junit.quickcheck.test.generator.Pair;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ResolvingGenericPropertyParameterTypesTest {
    @RunWith(JUnitQuickcheck.class)
    public static class PairProperties<F, S> {
        @Property public void firstAndSecond(F first, S second) {
            Pair p = new Pair<>(first, second, false);

            assertSame(first, p.first());
            assertSame(second, p.second());
        }
    }

    public static class FooPairProperties<F extends Foo, S extends Foo>
        extends PairProperties<F, S> {

        @Property public void bounded(Pair<F, S> p) {
            Foo first = p.first();
            Foo second = p.second();
        }
    }

    public static class ConcreteFooPairProperties extends FooPairProperties<Foo, Foo> {
    }

    public static class PairOfPairProperties<F, S, F1 extends F, F2 extends F, S1 extends S, S2 extends S>
        extends PairProperties<Pair<F1, S1>, Pair<F2, S2>> {

        @Property public void x(Pair<Pair<F1, S1>, Pair<F2, S2>> p) {
            Pair<F1, S1> first = p.first();
            Pair<F2, S2> second = p.second();
            F1 firstOfFirst = first.first();
            F2 firstOfSecond = second.first();
            S1 secondOfFirst = first.second();
            S2 secondOfSecond = second.second();
        }
    }

    @Test public void unresolvedFirstAndSecond() {
        assertThat(testResult(PairProperties.class), isSuccessful());
    }

    @Test public void unresolvedBoundedFirstAndSecond() {
        assertThat(testResult(FooPairProperties.class), isSuccessful());
    }

    @Test public void concreteFirstAndSecond() {
        assertThat(testResult(ConcreteFooPairProperties.class), isSuccessful());
    }

    @Ignore("Not sure whether a bug in generics resolution lib, unrealistic expectation, or pilot error")
    @Test public void unresolvedPairOfPair() {
        assertThat(testResult(PairOfPairProperties.class), isSuccessful());
    }
}

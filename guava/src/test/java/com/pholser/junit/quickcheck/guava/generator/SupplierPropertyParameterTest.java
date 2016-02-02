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

package com.pholser.junit.quickcheck.guava.generator;

import java.util.List;

import com.google.common.base.Supplier;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class SupplierPropertyParameterTest {
    @Test public void supplyingInts() {
        assertThat(testResult(SupplyingInts.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class SupplyingInts {
        @Property public void holds(Supplier<Integer> source) {
            int next = source.get();
        }
    }

    @Test public void supplyingUnresolvedLists() {
        assertThat(testResult(SupplyingUnresolvedLists.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class SupplyingUnresolvedLists<T> {
        @Property public void holds(Supplier<List<T>> source) {
            for (T each : source.get()) {
                // ensure types are ok
            }
        }
    }

    @Test public void supplyingUpperBounded() {
        assertThat(testResult(SupplyingUpperBounded.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class SupplyingUpperBounded {
        @Property public <T extends Integer> void holds(Supplier<? extends T[]> source) {
            T[] items = source.get();
            assumeThat(items.length, greaterThan(0));

            for (Integer each : items) {
                // ensure types are ok
            }
        }
    }
}

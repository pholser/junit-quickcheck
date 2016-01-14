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

import java.util.BitSet;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class BitSetPropertyParameterTypesTest {
    @Test public void bitSet() {
        assertThat(testResult(BitSetProperties.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class BitSetProperties {
        @Property public void shouldHold(BitSet s) {
        }
    }

    @Test public void shrinkingBitSet() {
        assertThat(testResult(ShrinkingBitSet.class), failureCountIs(1));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingBitSet {
        @Property public void shouldHold(BitSet s) {
            assumeThat(s.cardinality(), greaterThan(0));

            assertThat(s.cardinality(), lessThan(10));
        }
    }

    @Test public void shrinkingEmptyBitSet() {
        assertThat(testResult(ShrinkingEmptyBitSet.class), failureCountIs(1));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingEmptyBitSet {
        @Property public void shouldHold(BitSet s) {
            assumeThat(s.size(), equalTo(0));

            fail();
        }
    }
}

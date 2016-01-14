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

package com.pholser.junit.quickcheck.generator.java.util;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class OptionalLongPropertyParameterTest {
    @Test public void maybeALong() {
        assertThat(testResult(MaybeALong.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MaybeALong {
        @Property public void works(@InRange(minLong = -4L, maxLong = 5L) OptionalLong ell) {
            assumeTrue(ell.isPresent());
            assertThat(ell.getAsLong(), allOf(greaterThanOrEqualTo(-4L), lessThanOrEqualTo(5L)));
        }
    }

    @Test public void shrinking() {
        assertThat(testResult(ShrinkingOptionalLong.class), failureCountIs(1));
        assertTrue(ShrinkingOptionalLong.failed.stream().anyMatch(o -> !o.isPresent()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingOptionalLong {
        static List<OptionalLong> failed = new ArrayList<>();

        @Property public void works(OptionalLong optional) {
            failed.add(optional);

            fail();
        }
    }
}

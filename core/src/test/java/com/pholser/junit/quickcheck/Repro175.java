/*
 The MIT License

 Copyright (c) 2010-2017 Paul R. Holser, Jr.

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

import java.util.List;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.AnInt;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.util.Collections.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class Repro175 {
    @Test public void givesAllArgsAChanceToShrink() {
        assertThat(testResult(GivesAllArgsAChanceToShrink.class), failureCountIs(2));
        assertThat(Others.shrinkAttempts, greaterThan(1));
    }

    public static class Other {
    }

    public static class Others extends Generator<Other> {
        private static int shrinkAttempts;

        public Others() {
            super(Other.class);
        }

        @Override public List<Other> doShrink(SourceOfRandomness r, Other larger) {
            ++shrinkAttempts;
            return emptyList();
        }

        @Override public Other generate(SourceOfRandomness r, GenerationStatus s) {
            return new Other();
        }
    }

    @RunWith(JUnitQuickcheck.class)
    public static class GivesAllArgsAChanceToShrink {
        @Property public void shrinksInt(
            @From(AnInt.class) int test,
            @From(Others.class) Other other) {

            fail("Shrink me!");
        }

        @Property public void shrinksOther(
            @From(Others.class) Other other,
            @From(AnInt.class) int test) {

            fail("Shrink me!");
        }
    }
}

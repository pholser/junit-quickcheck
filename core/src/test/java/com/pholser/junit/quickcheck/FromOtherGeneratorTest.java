/*
 The MIT License

 Copyright (c) 2010-2013 Paul R. Holser, Jr.

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

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.*;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class FromOtherGeneratorTest {
    @Test public void explicitGeneratorTakesPrecedence() throws Exception {
        assertThat(testResult(WithExplicitGenerator.class), isSuccessful());
        assertEquals(asList(0, 1, 2, 3, 4), WithExplicitGenerator.values);
    }

    @RunWith(Theories.class)
    public static class WithExplicitGenerator {
        public static final List<Integer> values = new ArrayList<Integer>();

        @Theory public void shouldHold(@ForAll(sampleSize = 5) @From(Sequence.class) int i) {
            values.add(i);
        }
    }

    public static class Sequence extends Generator<Integer> {
        private int next = 0;

        public Sequence() {
            super(int.class);
        }

        @Override public Integer generate(SourceOfRandomness random, GenerationStatus status) {
            return next++;
        }
    }

    @Test public void typeMismatch() throws Exception {
        assertThat(testResult(WithGeneratorTypeThatDoesNotMatchTheoryParameterType.class),
            hasSingleFailureContaining(IllegalArgumentException.class.getName()));
    }

    @RunWith(Theories.class)
    public static class WithGeneratorTypeThatDoesNotMatchTheoryParameterType {
        @Theory public void shouldHold(@ForAll @From(StringEmitter.class) Number n) {
        }
    }

    public static class StringEmitter extends Generator<String> {
        public StringEmitter() {
            super(String.class);
        }

        @Override public String generate(SourceOfRandomness random, GenerationStatus status) {
            return "foo";
        }
    }
}

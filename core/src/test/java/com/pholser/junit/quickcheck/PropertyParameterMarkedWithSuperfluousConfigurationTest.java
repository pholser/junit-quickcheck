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

import java.util.Formatter;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.GeneratorConfigurationException;
import com.pholser.junit.quickcheck.generator.ValuesOf;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.Between;
import com.pholser.junit.quickcheck.test.generator.Foo;
import com.pholser.junit.quickcheck.test.generator.X;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class PropertyParameterMarkedWithSuperfluousConfigurationTest {
    @Test public void parameterMarkedWithUnexpectedConfiguration() {
        assertThat(
            testResult(WithSuperfluousConfiguration.class),
            hasSingleFailureContaining(GeneratorConfigurationException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WithSuperfluousConfiguration {
        @Property public void shouldHold(
            @ValuesOf @Between(min = 3, max = 6) Formatter.BigDecimalLayoutForm f) {
        }
    }

    @Test public void weedsOutCandidateGeneratorsThatDoNotSupportAConfigurationAnnotation() {
        assertThat(testResult(MultipleCandidateGenerators.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MultipleCandidateGenerators {
        @Property public void shouldHold(@From(Plain.class) @From(Markable.class) @X Foo f) {
            assertTrue(f.marked());
        }
    }

    @Test public void failsIfNoCandidateGeneratorsSupportAConfigurationAnnotation() {
        assertThat(
            testResult(MultipleCandidateGeneratorsButNoneUnderstandMarker.class),
            hasSingleFailureContaining(GeneratorConfigurationException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MultipleCandidateGeneratorsButNoneUnderstandMarker {
        @Property public void shouldHold(@From(Plain.class) @X Foo f) {
            assertTrue(f.marked());
        }
    }

    public static class Markable extends Generator<Foo> {
        private X x;

        public Markable() {
            super(Foo.class);
        }

        @Override public Foo generate(SourceOfRandomness random, GenerationStatus status) {
            return new Foo(random.nextInt(), x != null);
        }

        public void configure(X x) {
            this.x = x;
        }
    }

    public static class Plain extends Generator<Foo> {
        public Plain() {
            super(Foo.class);
        }

        @Override public Foo generate(SourceOfRandomness random, GenerationStatus status) {
            return new Foo(random.nextInt(), false);
        }
    }
}

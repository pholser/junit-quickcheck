/*
 The MIT License

 Copyright (c) 2010-2014 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.internal.generator;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.GeometricDistribution;
import com.pholser.junit.quickcheck.internal.ParameterContext;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Parameter;

import static java.util.Arrays.*;
import static org.junit.rules.ExpectedException.*;

@RunWith(MockitoJUnitRunner.class)
public class GenerationContextTest {
    @Rule public final ExpectedException thrown = none();

    @Mock private SourceOfRandomness random;

    @Test public void whenDiscardRatioExceededEvenWithSomeSuccesses() throws Exception {
        ParameterContext parameter =
            new ParameterContext("arg", annotatedType(), "declarer")
                .annotate(annotatedElement());

        GenerationContext gen = new GenerationContext(
            parameter,
            new GeneratorRepository(null).register(new Countdown()),
            new GeometricDistribution(),
            random);

        thrown.expect(GenerationContext.DiscardRatioExceededException.class);
        thrown.expectMessage(String.format(
            GenerationContext.DiscardRatioExceededException.MESSAGE_TEMPLATE,
            parameter.discardRatio(),
            30,
            10,
            3D));

        while (gen.shouldContinue())
            gen.generate(random);
    }

    public static void parameterHaver(@ForAll(sampleSize = 20, discardRatio = 3, suchThat = "#_ > 0") int x) {
    }

    private AnnotatedElement annotatedElement() throws Exception {
        return parameter();
    }

    private AnnotatedType annotatedType() throws Exception {
        return parameter().getAnnotatedType();
    }

    private Parameter parameter() throws Exception {
        return getClass().getMethod("parameterHaver", int.class).getParameters()[0];
    }

    public static class Countdown extends Generator<Integer> {
        private int count = 10;

        public Countdown() {
            super(asList(int.class, Integer.class));
        }

        @Override
        public Integer generate(SourceOfRandomness random, GenerationStatus status) {
            return count--;
        }
    }
}

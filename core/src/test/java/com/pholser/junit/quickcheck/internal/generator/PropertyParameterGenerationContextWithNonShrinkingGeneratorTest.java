/*
 The MIT License

 Copyright (c) 2010-2021 Paul R. Holser, Jr.

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

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.GeometricDistribution;
import com.pholser.junit.quickcheck.internal.ParameterTypeContext;
import com.pholser.junit.quickcheck.internal.PropertyParameterContext;
import com.pholser.junit.quickcheck.internal.sampling.TupleParameterSampler;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Parameter;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class PropertyParameterGenerationContextWithNonShrinkingGeneratorTest {
    @Rule public final MockitoRule rule = MockitoJUnit.rule();

    @Mock private SourceOfRandomness random;

    @Test public void givesNoShrunkenValues() throws Exception {
        PropertyParameterContext parameter =
            new PropertyParameterContext(
                ParameterTypeContext.forParameter(parameter())
                    .annotate(annotatedElement()));

        PropertyParameterGenerationContext gen =
            new PropertyParameterGenerationContext(
                parameter,
                new GeneratorRepository(random).register(new NonShrinker()),
                new GeometricDistribution(),
                random,
                new TupleParameterSampler(20));

        assertEquals(emptyList(), gen.shrink(0));
    }

    public static void parameterHaver(int x) {
    }

    private AnnotatedElement annotatedElement() throws Exception {
        return parameter();
    }

    private AnnotatedType annotatedType() throws Exception {
        return parameter().getAnnotatedType();
    }

    private Parameter parameter() throws Exception {
        return getClass().getMethod("parameterHaver", int.class)
            .getParameters()[0];
    }

    public static class NonShrinker extends Generator<Integer> {
        public NonShrinker() {
            super(asList(Integer.class, int.class));
        }

        @Override public Integer generate(
            SourceOfRandomness random,
            GenerationStatus status) {

            return random.nextInt();
        }

        @Override public boolean canShrink(Object larger) {
            return false;
        }
    }
}

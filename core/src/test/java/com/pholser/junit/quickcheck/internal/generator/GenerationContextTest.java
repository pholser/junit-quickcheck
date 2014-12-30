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

import com.pholser.junit.quickcheck.AnnotatedTypes;
import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.SuchThat;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.ParameterContext;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Arrays.*;
import static org.junit.rules.ExpectedException.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GenerationContextTest {
    @Rule public final ExpectedException thrown = none();

    @Mock private SourceOfRandomness random;
    @Mock private Generator<Integer> generator;
    @Mock private ForAll quantifier;
    @Mock private SuchThat constraint;

    @SuppressWarnings("unchecked")
    @Before public void beforeEach() {
        when(quantifier.sampleSize()).thenReturn(20);
        when(quantifier.discardRatio()).thenReturn(3);
        when(generator.types()).thenReturn(asList(int.class));
        when(generator.canRegisterAsType(any(Class.class))).thenReturn(true);
        when(generator.generate(same(random), any(GenerationStatus.class)))
            .thenReturn(10).thenReturn(9).thenReturn(8).thenReturn(7).thenReturn(6)
            .thenReturn(5).thenReturn(4).thenReturn(3).thenReturn(2).thenReturn(1)
            .thenReturn(0);
        when(constraint.value()).thenReturn("#_ > 0");
    }

    @Test public void whenDiscardRatioExceededEvenWithSomeSuccesses() {
        ParameterContext parameter = new ParameterContext(AnnotatedTypes.from(int.class));
        parameter.addQuantifier(quantifier);
        parameter.addConstraint(constraint);

        GenerationContext generation =
            new GenerationContext(parameter, new GeneratorRepository(random).register(generator));

        thrown.expect(GenerationContext.DiscardRatioExceededException.class);
        thrown.expectMessage(String.format(
            GenerationContext.DiscardRatioExceededException.MESSAGE_TEMPLATE,
            parameter.discardRatio(),
            30,
            10,
            3D));

        while (generation.shouldContinue())
            generation.generate(random);
    }
}

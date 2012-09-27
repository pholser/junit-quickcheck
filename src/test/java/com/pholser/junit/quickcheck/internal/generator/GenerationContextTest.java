package com.pholser.junit.quickcheck.internal.generator;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.SuchThat;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.ParameterContext;
import com.pholser.junit.quickcheck.generator.SourceOfRandomness;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static java.util.Arrays.*;
import static org.junit.rules.ExpectedException.*;
import static org.mockito.Mockito.*;

public class GenerationContextTest {
    @Rule public final ExpectedException thrown = none();

    @Mock private SourceOfRandomness random;
    @Mock private Generator<Integer> generator;
    @Mock private ForAll quantifier;
    @Mock private SuchThat constraint;

    @Before
    @SuppressWarnings("unchecked")
    public void beforeEach() {
        MockitoAnnotations.initMocks(this);
        when(quantifier.sampleSize()).thenReturn(20);
        when(quantifier.discardRatio()).thenReturn(3);
        when(generator.types()).thenReturn(asList(int.class));
        when(generator.generate(same(random), any(GenerationStatus.class))).thenReturn(10).thenReturn(9).thenReturn(8)
            .thenReturn(7).thenReturn(6).thenReturn(5).thenReturn(4).thenReturn(3).thenReturn(2).thenReturn(1)
            .thenReturn(0);
        when(constraint.value()).thenReturn("#root > 0");
    }

    @Test
    public void whenDiscardRatioExceededEvenWithSomeSuccesses() {
        ParameterContext parameter = new ParameterContext(int.class);
        parameter.addQuantifier(quantifier);
        parameter.addConstraint(constraint);

        GenerationContext generation =
            new GenerationContext(parameter, new GeneratorRepository(random).add(generator));

        thrown.expect(AssumptionViolatedException.class);
        thrown.expectMessage(String.format(GenerationContext.DISCARD_RATIO_MESSAGE, 3, 30, 10, 3D));

        while (generation.shouldContinue())
            generation.generate(random);
    }
}

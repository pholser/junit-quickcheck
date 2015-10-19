package com.pholser.junit.quickcheck.internal.generator;

import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.Weighted;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CompositeGeneratorTest {
    @Rule public final MockitoRule mockito = MockitoJUnit.rule();

    private CompositeGenerator composite;
    @Mock private Generator<Integer> first;
    @Mock private Generator<Integer> second;
    @Mock private Generator<Integer> third;
    @Mock private SourceOfRandomness random;

    @Before public void setUp() {
        composite = new CompositeGenerator(asList(
            new Weighted<>(first, 2),
            new Weighted<>(second, 5),
            new Weighted<>(third, 7)
        ));
    }

    @Test public void capabilityOfShrinkingDependsOnComponents() {
        when(first.canShrink(9)).thenReturn(true);
        when(second.canShrink(9)).thenReturn(false);
        when(third.canShrink(9)).thenReturn(true);

        assertTrue(composite.canShrink(9));
    }

    @Test public void capabilityOfShrinkingFalseIfNoComponentsCanShrinkAValue() {
        when(first.canShrink(8)).thenReturn(false);
        when(second.canShrink(8)).thenReturn(false);
        when(third.canShrink(8)).thenReturn(false);

        assertFalse(composite.canShrink(8));
    }

    @Test public void shrinkingChoosesAComponentCapableOfShrinkingTheValue() {
        stub(first.canShrink(7)).toReturn(true);
        stub(second.canShrink(7)).toReturn(false);
        stub(third.canShrink(7)).toReturn(true);
        when(first.types()).thenReturn(singletonList(Integer.class));
        when(first.doShrink(random, 7)).thenReturn(asList(3, 6));
        when(random.nextInt(9)).thenReturn(1);

        assertEquals(asList(3, 6), composite.shrink(random, 7));
        verify(first, atLeastOnce()).doShrink(random, 7);
    }
}

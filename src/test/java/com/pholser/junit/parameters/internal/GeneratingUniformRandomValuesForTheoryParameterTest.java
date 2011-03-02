package com.pholser.junit.parameters.internal;

import java.lang.reflect.Type;
import java.util.List;

import com.pholser.junit.parameters.Arrays;
import com.pholser.junit.parameters.ForAll;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.PotentialAssignment;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public abstract class GeneratingUniformRandomValuesForTheoryParameterTest {
    protected SourceOfRandomness random;
    private ForAll quantifier;
    private List<PotentialAssignment> theoryParms;

    @Before
    public final void setUp() {
        random = mock(SourceOfRandomness.class);
        primeSourceOfRandomness();
        quantifier = mock(ForAll.class);
        primeSampleSize();
        RandomTheoryParameterGenerator generator = new RandomTheoryParameterGenerator(random);

        theoryParms = generator.generate(quantifier, parameterType());
    }

    private void primeSampleSize() {
        when(quantifier.sampleSize()).thenReturn(sampleSize());
    }

    protected abstract void primeSourceOfRandomness();

    protected abstract Type parameterType();

    protected abstract int sampleSize();

    protected abstract List<?> randomValues();

    @Test
    public final void respectsSampleSize() {
        assertEquals(quantifier.sampleSize(), theoryParms.size());
    }

    @Test
    public final void insertsTheRandomValuesIntoAssignments() throws Exception {
        List<?> values = randomValues();
        assertEquals(sampleSize(), values.size());
        for (int i = 0; i < values.size(); ++i) {
            Object expected = values.get(i);
            if (expected.getClass().isArray()) {
                assertEquals(i + "'th value, ",
                    Arrays.toList(expected),
                    Arrays.toList(theoryParms.get(i).getValue()));
            }
            else
                assertEquals(i + "'th value", values.get(i), theoryParms.get(i).getValue());
        }
    }

    @Test
    public abstract void verifyInteractionWithRandomness();
}

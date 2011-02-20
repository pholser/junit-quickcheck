package com.pholser.junit.parameters.internal;

import java.util.List;

import com.pholser.junit.parameters.ForAll;
import org.junit.Test;
import org.junit.experimental.theories.PotentialAssignment;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GeneratingRandomValuesForTheoryParametersTest {
    @Test
    public void respectsSampleSize() throws Exception {
        SourceOfRandomness random = mock(SourceOfRandomness.class);
        when(random.nextInt()).thenReturn(-1).thenReturn(-2);
        ForAll quantifier = mock(ForAll.class);
        when(quantifier.sampleSize()).thenReturn(2);
        RandomTheoryParameterGenerator generator = new RandomTheoryParameterGenerator(random);

        List<PotentialAssignment> theoryParms = generator.generate(quantifier, int.class);

        assertEquals(quantifier.sampleSize(), theoryParms.size());
        assertEquals(Integer.valueOf(-1), theoryParms.get(0).getValue());
        assertEquals(Integer.valueOf(-2), theoryParms.get(1).getValue());
        verify(random, times(2)).nextInt();
    }
}

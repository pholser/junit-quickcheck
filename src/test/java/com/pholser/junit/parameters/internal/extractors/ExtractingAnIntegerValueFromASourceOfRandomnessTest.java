package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.random.SourceOfRandomness;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ExtractingAnIntegerValueFromASourceOfRandomnessTest {
    @Test
    public void interaction() {
        SourceOfRandomness random = mock(SourceOfRandomness.class);
        when(random.nextInt()).thenReturn(-1);

        assertEquals(Integer.valueOf(-1), new IntegerExtractor().randomValue(random));
    }
}

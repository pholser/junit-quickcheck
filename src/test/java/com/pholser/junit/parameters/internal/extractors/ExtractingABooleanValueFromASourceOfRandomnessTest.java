package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.random.SourceOfRandomness;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ExtractingABooleanValueFromASourceOfRandomnessTest {
    @Test
    public void interaction() {
        SourceOfRandomness random = mock(SourceOfRandomness.class);
        when(random.nextBoolean()).thenReturn(true);

        assertTrue(new BooleanExtractor().randomValue(random));
    }
}

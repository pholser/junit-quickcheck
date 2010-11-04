package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.random.SourceOfRandomness;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ExtractingADoubleValueFromASourceOfRandomnessTest {
    @Test
    public void interaction() {
        SourceOfRandomness random = mock(SourceOfRandomness.class);
        when(random.nextDouble()).thenReturn(1.2D);

        assertEquals(Double.valueOf(1.2D), new DoubleExtractor().randomValue(random));
    }
}

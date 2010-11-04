package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.random.SourceOfRandomness;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ExtractingAStringValueFromASourceOfRandomnessTest {
    @Test
    public void interaction() throws Exception {
        SourceOfRandomness random = mock(SourceOfRandomness.class);
        when(random.nextBytes(16)).thenReturn("0123456789ABCDEF".getBytes("US-ASCII"));

        assertEquals("0123456789ABCDEF", new StringExtractor().randomValue(random));
    }
}

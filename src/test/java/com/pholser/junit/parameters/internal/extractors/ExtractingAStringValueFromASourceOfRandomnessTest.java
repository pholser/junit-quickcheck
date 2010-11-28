package com.pholser.junit.parameters.internal.extractors;

import com.pholser.junit.parameters.random.SourceOfRandomness;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ExtractingAStringValueFromASourceOfRandomnessTest {
    private SourceOfRandomness random;

    @Before
    public void setUp() throws Exception {
        random = mock(SourceOfRandomness.class);
        when(random.nextBytes(16)).thenReturn("0123456789ABCDEF".getBytes("US-ASCII"));
    }

    @Test
    public void interaction() {
        assertEquals("0123456789ABCDEF", new StringExtractor().randomValue(random));
    }

    @Test(expected = AssertionError.class)
    public void unsupportedEncoding() {
        new StringExtractor("@#!@#!@#").randomValue(random);
    }
}

package com.pholser.junit.parameters.internal.extractors;

import java.util.Date;

import com.pholser.junit.parameters.random.SourceOfRandomness;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ExtractingADateValueFromASourceOfRandomnessTest {
    @Test
    public void interaction() {
        SourceOfRandomness random = mock(SourceOfRandomness.class);
        when(random.nextLong()).thenReturn(0L);

        assertEquals(new Date(0L), new DateExtractor().randomValue(random));
    }
}

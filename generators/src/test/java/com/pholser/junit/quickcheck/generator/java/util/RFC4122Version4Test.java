package com.pholser.junit.quickcheck.generator.java.util;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.*;

import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.generator.CoreTheoryParameterTest;

import static com.pholser.junit.quickcheck.generator.java.util.RFC4122.*;
import static org.mockito.Mockito.*;

public class RFC4122Version4Test extends CoreTheoryParameterTest {
    @SuppressWarnings("unchecked")
    @Override protected Class<? extends Generator>[] explicitGenerators() {
        return (Class<? extends Generator>[]) new Class<?>[] { Version4.class };
    }

    @Override protected void primeSourceOfRandomness() throws Exception {
        when(randomForParameterGenerator.nextBytes(16))
            .thenReturn(new byte[] { 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xa, 0xb, 0xc, 0xd, 0xe, 0xf })
            .thenReturn(new byte[] { 0x8, 0x9, 0xa, 0xb, 0xc, 0xd, 0xe, 0xf, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7 });
    }

    @Override protected Type parameterType() {
        return UUID.class;
    }

    @Override protected int sampleSize() {
        return 2;
    }

    @Override protected List<?> randomValues() {
        return asList(UUID.fromString("00010203-0405-4607-8809-0a0b0c0d0e0f"),
            UUID.fromString("08090a0b-0c0d-4e0f-8001-020304050607"));
    }

    @Override public void verifyInteractionWithRandomness() {
        verify(randomForParameterGenerator, times(2)).nextBytes(16);
    }
}

package com.pholser.junit.parameters.random;

import java.security.SecureRandom;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.powermock.reflect.Whitebox.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SecureJDKSourceOfRandomness.class)
public class ConstructionOfSecureJDKSourceOfRandomnessTest {
    @Test
    public void zeroArg() throws Exception {
        SecureRandom secure = mock(SecureRandom.class);
        whenNew(SecureRandom.class).withNoArguments().thenReturn(secure);

        SecureJDKSourceOfRandomness source = new SecureJDKSourceOfRandomness();

        assertSame(secure, getInternalState(source, Random.class));
    }

    @Test
    public void withSeed() throws Exception {
        byte[] seed = new byte[0];
        SecureRandom secure = mock(SecureRandom.class);
        whenNew(SecureRandom.class).withParameterTypes(byte[].class).withArguments(seed).thenReturn(secure);

        SecureJDKSourceOfRandomness source = new SecureJDKSourceOfRandomness(seed);

        assertSame(secure, getInternalState(source, Random.class));
    }
}

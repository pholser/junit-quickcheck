package com.pholser.junit.parameters.random;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.powermock.reflect.Whitebox.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JDKSourceOfRandomness.class)
public class ConstructionOfJDKSourceOfRandomnessTest {
    @Test
    public void construction() throws Exception {
        Random random = mock(Random.class);
        whenNew(Random.class).withNoArguments().thenReturn(random);

        JDKSourceOfRandomness source = new JDKSourceOfRandomness();

        assertSame(random, getInternalState(source, Random.class));
    }
}

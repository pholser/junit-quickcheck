package com.pholser.junit.quickcheck;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.pholser.junit.quickcheck.ExceptionMatchers.*;
import static org.junit.rules.ExpectedException.*;

public abstract class UtilityClassHarness {
    @Rule public final ExpectedException thrown = none();

    private final Class<?> utility;

    protected UtilityClassHarness(Class<?> utility) {
        this.utility = utility;
    }

    @Test
    public final void attemptInstantiation() throws Exception {
        Constructor<?> constructor = utility.getDeclaredConstructor();
        constructor.setAccessible(true);

        thrown.expect(InvocationTargetException.class);
        thrown.expect(targetOfType(UnsupportedOperationException.class));

        constructor.newInstance();
    }
}

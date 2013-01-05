package com.pholser.junit.quickcheck.generator;

import com.google.common.base.Predicate;
import org.junit.Before;
import org.junit.Test;

import static com.pholser.junit.quickcheck.generator.Lambdas.*;
import static org.junit.Assert.*;

public class LambdasObjectMethodsTest {
    private BooleanGenerator returnValueGenerator;
    private Predicate<?> predicate;

    @Before
    public void setUp() {
        returnValueGenerator = new BooleanGenerator();
        predicate = makeLambda(Predicate.class, returnValueGenerator, null);
    }

    @Test
    public void equalsBasedOnIdentity() {
        Predicate<?> duplicate = makeLambda(Predicate.class, returnValueGenerator, null);

        assertEquals(predicate, predicate);
        assertEquals(duplicate, duplicate);
        assertNotEquals(predicate, duplicate);
        assertNotEquals(duplicate, predicate);
    }

    @Test
    public void hashCodeBasedOnIdentity() {
        assertEquals(System.identityHashCode(predicate), predicate.hashCode());
    }

    @Test
    public void toStringGivesAnIndicationOfItsRandomGeneration() {
        assertEquals("a randomly generated instance of " + Predicate.class, predicate.toString());
    }
}

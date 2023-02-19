/*
 The MIT License

 Copyright (c) 2010-2021 Paul R. Holser, Jr.

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.pholser.junit.quickcheck.generator;

import static com.pholser.junit.quickcheck.generator.Lambdas.makeLambda;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import com.pholser.junit.quickcheck.internal.GeometricDistribution;
import com.pholser.junit.quickcheck.internal.generator.SimpleGenerationStatus;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.test.generator.ABool;
import com.pholser.junit.quickcheck.test.generator.AnInt;
import java.util.Random;
import java.util.function.Predicate;
import org.junit.Before;
import org.junit.Test;

public class LambdasTest {
    private ABool returnValueGenerator;
    private SourceOfRandomness random;
    private SimpleGenerationStatus status;
    private Predicate<Integer> predicate;

    @SuppressWarnings("unchecked")
    @Before public void setUp() {
        returnValueGenerator = new ABool();
        random = new SourceOfRandomness(new Random());
        random.setSeed(-1L);
        status =
            new SimpleGenerationStatus(
                new GeometricDistribution(),
                random,
                0);

        predicate =
            makeLambda(Predicate.class, returnValueGenerator, random, status);
    }

    @Test public void equalsBasedOnIdentity() {
        Predicate<?> duplicate =
            makeLambda(Predicate.class, returnValueGenerator, random, status);

        assertEquals(predicate, predicate);
        assertEquals(duplicate, duplicate);
        assertNotEquals(predicate, duplicate);
        assertNotEquals(duplicate, predicate);
    }

    @Test public void hashCodeBasedOnIdentity() {
        assertEquals(System.identityHashCode(predicate), predicate.hashCode());
    }

    @Test public void toStringGivesAnIndicationOfItsRandomGeneration() {
        assertEquals(
            "a randomly generated instance of " + Predicate.class,
            predicate.toString());
    }

    @Test public void rejectsNonFunctionalInterface() {
        IllegalArgumentException ex =
            assertThrows(
                IllegalArgumentException.class,
                () -> makeLambda(Cloneable.class, new AnInt(), random, status));
        assertEquals(
            Cloneable.class + " is not a functional interface type",
            ex.getMessage());
    }

    @Test public void invokingDefaultMethodOnFunctionalInterface() {
        @SuppressWarnings("unchecked")
        Predicate<Integer> another =
            makeLambda(Predicate.class, returnValueGenerator, random, status);

        boolean firstResult = predicate.test(4);
        boolean secondResult = another.test(4);

        assertEquals(
            firstResult || secondResult,
            predicate.or(another).test(4));
    }
}

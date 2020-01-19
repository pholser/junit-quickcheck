/*
 The MIT License

 Copyright (c) 2010-2020 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck;

import java.util.Collections;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static java.util.Collections.*;
import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.*;

public class BasicCapabilitiesOfGeneratorTest {
    @Rule public final ExpectedException thrown = none();

    private Generator<Object> generator;
    private Generator<Object> nonShrinkingGenerator;

    @Before public void beforeEach() {
        generator = new Generator<Object>(Object.class) {
            @Override public Object generate(SourceOfRandomness random, GenerationStatus status) {
                return this;
            }
        };

        nonShrinkingGenerator = new Generator<Object>(Object.class) {
            @Override public Object generate(SourceOfRandomness random, GenerationStatus status) {
                return this;
            }

            @Override public boolean canShrink(Object larger) {
                return false;
            }
        };
    }

    @Test public void noComponents() {
        assertFalse(generator.hasComponents());
    }

    @Test public void numberOfNeededComponents() {
        assertEquals(0, generator.numberOfNeededComponents());
    }

    @Test public void addingComponentsDoesNothing() {
        generator.addComponentGenerators(Collections.<Generator<?>> emptyList());
    }

    @Test public void abilityToShrink() {
        assertTrue(generator.canShrink(new Object()));
    }

    @Test public void actOfShrinking() {
        assertEquals(emptyList(), generator.shrink(null, new Object()));
    }

    @Test public void nonShrinkingGeneratorAttemptingToShrink() {
        thrown.expect(IllegalStateException.class);

        nonShrinkingGenerator.shrink(null, new Object());
    }
}

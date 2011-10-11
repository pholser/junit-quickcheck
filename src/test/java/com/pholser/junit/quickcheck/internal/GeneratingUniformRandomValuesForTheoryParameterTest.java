/*
 The MIT License

 Copyright (c) 2010-2011 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.internal;

import java.lang.reflect.Type;
import java.util.List;

import com.pholser.junit.quickcheck.Arrays;
import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.internal.generate.RandomTheoryParameterGenerator;
import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.PotentialAssignment;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public abstract class GeneratingUniformRandomValuesForTheoryParameterTest {
    protected SourceOfRandomness random;
    private ForAll quantifier;
    private List<PotentialAssignment> theoryParms;

    @Before
    public final void setUp() {
        random = mock(SourceOfRandomness.class);
        primeSourceOfRandomness();
        quantifier = mock(ForAll.class);
        primeSampleSize();
        RandomTheoryParameterGenerator generator = new RandomTheoryParameterGenerator(random);

        theoryParms = generator.generate(quantifier, parameterType());
    }

    private void primeSampleSize() {
        when(quantifier.sampleSize()).thenReturn(sampleSize());
    }

    protected abstract void primeSourceOfRandomness();

    protected abstract Type parameterType();

    protected abstract int sampleSize();

    protected abstract List<?> randomValues();

    @Test
    public final void respectsSampleSize() {
        assertEquals(quantifier.sampleSize(), theoryParms.size());
    }

    @Test
    public final void insertsTheRandomValuesIntoAssignments() throws Exception {
        List<?> values = randomValues();
        assertEquals(sampleSize(), values.size());
        for (int i = 0; i < values.size(); ++i) {
            Object expected = values.get(i);
            if (expected.getClass().isArray()) {
                assertEquals(i + "'th value, ",
                    Arrays.toList(expected),
                    Arrays.toList(theoryParms.get(i).getValue()));
            }
            else
                assertEquals(i + "'th value", values.get(i), theoryParms.get(i).getValue());
        }
    }

    @Test
    public abstract void verifyInteractionWithRandomness();
}

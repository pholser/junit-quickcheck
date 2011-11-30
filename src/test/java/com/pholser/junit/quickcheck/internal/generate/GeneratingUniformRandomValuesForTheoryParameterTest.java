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

package com.pholser.junit.quickcheck.internal.generate;

import java.lang.reflect.Type;
import java.util.List;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.RandomValueExtractor;
import com.pholser.junit.quickcheck.internal.ParameterContext;
import com.pholser.junit.quickcheck.internal.extractors.BasicExtractorSource;
import com.pholser.junit.quickcheck.internal.extractors.ExtractorRepository;
import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;
import org.junit.Before;
import org.junit.Test;
import org.junit.contrib.theories.PotentialAssignment;

import static com.pholser.junit.quickcheck.Objects.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public abstract class GeneratingUniformRandomValuesForTheoryParameterTest {
    protected SourceOfRandomness random;
    protected BasicExtractorSource source;
    private ForAll quantifier;
    private From explicitExtractors;
    private List<PotentialAssignment> theoryParms;

    @Before
    public final void setUp() {
        random = mock(SourceOfRandomness.class);
        source = new BasicExtractorSource();
        primeSourceOfRandomness();
        quantifier = mock(ForAll.class);
        explicitExtractors = mock(From.class);
        primeSampleSize();
        primeExplicitExtractors();

        RandomTheoryParameterGenerator generator =
            new RandomTheoryParameterGenerator(random, new ExtractorRepository().add(source));

        ParameterContext context = new ParameterContext(parameterType());
        context.addQuantifier(quantifier);
        if (explicitExtractors.value() != null)
            context.addExtractors(explicitExtractors);
        theoryParms = generator.generate(context);
    }

    private void primeSampleSize() {
        when(quantifier.sampleSize()).thenReturn(sampleSize());
    }

    private void primeExplicitExtractors() {
        when(explicitExtractors.value()).thenReturn(explicitExtractors());
    }

    protected abstract void primeSourceOfRandomness();

    protected abstract Type parameterType();

    protected abstract int sampleSize();

    protected Class<? extends RandomValueExtractor>[] explicitExtractors() {
        return null;
    }

    protected abstract List<?> randomValues();

    @Test
    public final void respectsSampleSize() {
        assertEquals(quantifier.sampleSize(), theoryParms.size());
    }

    @Test
    public final void insertsTheRandomValuesIntoAssignments() throws Exception {
        List<?> values = randomValues();
        assertEquals(sampleSize(), values.size());
        for (int i = 0; i < values.size(); ++i)
            assertThat(i + "'th value", theoryParms.get(i).getValue(), deepEquals(values.get(i)));
    }

    @Test
    public abstract void verifyInteractionWithRandomness();
}

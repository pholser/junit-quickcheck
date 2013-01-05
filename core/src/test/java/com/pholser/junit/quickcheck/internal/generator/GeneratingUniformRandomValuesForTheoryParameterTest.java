/*
 The MIT License

 Copyright (c) 2010-2013 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.internal.generator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.SuchThat;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.ParameterContext;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.Before;
import org.junit.Test;
import org.junit.contrib.theories.PotentialAssignment;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.pholser.junit.quickcheck.Objects.*;
import static java.util.Collections.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public abstract class GeneratingUniformRandomValuesForTheoryParameterTest {
    protected static final String PARAMETER_NAME = "arg0";

    @Mock protected SourceOfRandomness randomForParameterGenerator;
    @Mock protected SourceOfRandomness randomForGeneratorRepo;
    @Mock private ForAll quantifier;
    @Mock private From explicitGenerators;
    @Mock private SuchThat constraint;
    protected BasicGeneratorSource source;
    protected GeneratorRepository repository;
    private List<PotentialAssignment> theoryParameters;

    @Before
    public final void beforeEach() throws Exception {
        MockitoAnnotations.initMocks(this);

        source = new BasicGeneratorSource();
        primeSourceOfRandomness();
        primeSampleSize();
        primeExplicitGenerators();
        primeConstraint();

        repository = new GeneratorRepository(randomForGeneratorRepo).add(source);
        Iterable<Generator<?>> auxiliarySource = auxiliaryGeneratorSource();
        if (auxiliarySource != null)
            repository.add(auxiliarySource);

        RandomTheoryParameterGenerator generator =
            new RandomTheoryParameterGenerator(randomForParameterGenerator, repository);

        ParameterContext context = new ParameterContext(parameterType(), PARAMETER_NAME);
        context.addQuantifier(quantifier);
        context.addConstraint(constraint);
        if (explicitGenerators.value() != null)
            context.addGenerators(explicitGenerators);
        for (Map.Entry<Class<? extends Annotation>, Annotation> each : configurations().entrySet())
            context.addConfiguration(each.getKey(), each.getValue());

        theoryParameters = generator.generate(context);
    }

    private void primeSampleSize() {
        when(quantifier.sampleSize()).thenReturn(sampleSize());
    }

    private void primeExplicitGenerators() {
        when(explicitGenerators.value()).thenReturn(explicitGenerators());
    }

    private void primeConstraint() {
        when(constraint.value()).thenReturn(constraintExpression());
    }

    protected Iterable<Generator<?>> auxiliaryGeneratorSource() {
        return null;
    }

    protected abstract void primeSourceOfRandomness() throws Exception;

    protected abstract Type parameterType();

    protected abstract int sampleSize();

    protected Map<Class<? extends Annotation>, Annotation> configurations() {
        return emptyMap();
    }

    protected String constraintExpression() {
        return null;
    }

    protected Class<? extends Generator>[] explicitGenerators() {
        return null;
    }

    protected abstract List<?> randomValues();

    @Test
    public final void respectsSampleSize() {
        assertEquals(quantifier.sampleSize(), theoryParameters.size());
    }

    @Test
    public final void insertsTheRandomValuesIntoAssignments() throws Exception {
        assertEquals(sampleSize(), theoryParameters.size());

        List<?> values = randomValues();
        assertEquals(sampleSize(), values.size());

        for (int i = 0; i < theoryParameters.size(); ++i)
            verifyEquivalenceOfTheoryParameter(i, values.get(i), theoryParameters.get(i).getValue());
    }

    protected void verifyEquivalenceOfTheoryParameter(int index, Object expected, Object actual) {
        assertThat(index + "'th value", expected, deepEquals(actual));
    }

    @Test
    public abstract void verifyInteractionWithRandomness();
}

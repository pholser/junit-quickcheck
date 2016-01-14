/*
 The MIT License

 Copyright (c) 2010-2016 Paul R. Holser, Jr.

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

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.When;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.GeometricDistribution;
import com.pholser.junit.quickcheck.internal.ParameterTypeContext;
import com.pholser.junit.quickcheck.internal.PropertyParameterContext;
import com.pholser.junit.quickcheck.internal.Reflection;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.test.generator.TestGeneratorSource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;

import static com.pholser.junit.quickcheck.Objects.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public abstract class CorePropertyParameterTest {
    @Rule public final MockitoRule mockito = MockitoJUnit.rule();

    @Mock protected SourceOfRandomness randomForParameterGenerator;
    @Mock protected SourceOfRandomness randomForGeneratorRepo;
    @Mock protected GeometricDistribution distro;
    @Mock protected Logger log;
    protected Iterable<Generator<?>> source;
    protected GeneratorRepository repository;

    @Mock private Property propertyMetadata;
    @Mock private When quantifier;
    @Mock private From explicitGenerators;
    private List<Object> propertyParameters = new ArrayList<>();

    @Before public final void beforeEach() throws Exception {
        source = generatorSource();
        primeSourceOfRandomness();
        primeSeed();
        primeTrials();

        repository = new GeneratorRepository(randomForGeneratorRepo).register(source);
        Iterable<Generator<?>> auxiliarySource = auxiliaryGeneratorSource();
        if (auxiliarySource != null)
            repository.register(auxiliarySource);

        AnnotatedType type = annotatedType();
        int trials = trials();
        PropertyParameterContext parameter =
            new PropertyParameterContext(
                new ParameterTypeContext("arg", type, type.toString()),
                trials)
                .annotate(annotatedElement());
        parameter.addQuantifier(quantifier);

        PropertyParameterGenerationContext generator =
            new PropertyParameterGenerationContext(
                parameter,
                repository,
                distro,
                randomForParameterGenerator,
                log);
        for (int i = 0; i < trials; ++i)
            propertyParameters.add(generator.generate());
    }

    protected Iterable<Generator<?>> generatorSource() {
        return new TestGeneratorSource();
    }

    private void primeTrials() {
        when(propertyMetadata.trials()).thenReturn(trials());
    }

    private void primeSeed() {
        long seed = seed();
        when(quantifier.seed()).thenReturn(seed);
        when(randomForParameterGenerator.seed()).thenReturn(seed);
    }

    protected long seed() {
        return (long) Reflection.defaultValueOf(When.class, "seed");
    }

    protected Iterable<Generator<?>> auxiliaryGeneratorSource() {
        return null;
    }

    protected abstract void primeSourceOfRandomness() throws Exception;

    protected final AnnotatedType annotatedType() throws Exception {
        try {
            return typeBearerField().getAnnotatedType();
        } catch (Exception e) {
            return typeBearerParameter().getAnnotatedType();
        }
    }

    protected final AnnotatedElement annotatedElement() throws Exception {
        try {
            return typeBearerField();
        } catch (Exception e) {
            return typeBearerParameter();
        }
    }

    private Field typeBearerField() throws Exception {
        return getClass().getField("TYPE_BEARER");
    }

    private Parameter typeBearerParameter() throws Exception {
        return getClass().getMethod("TYPE_BEARER", Integer.class).getParameters()[0];
    }

    protected abstract int trials();

    protected abstract List<?> randomValues();

    @Test public final void producesExpectedRandomValues() throws Exception {
        List<?> values = randomValues();
        assertEquals(trials(), values.size());

        for (int i = 0; i < propertyParameters.size(); ++i)
            verifyEquivalenceOfPropertyParameter(i, values.get(i), propertyParameters.get(i));
    }

    protected void verifyEquivalenceOfPropertyParameter(int index, Object expected, Object actual) throws Exception {
        assertThat(index + "'th value", expected, deepEquals(actual));
    }

    @Test public abstract void verifyInteractionWithRandomness();
}

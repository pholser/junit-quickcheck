/*
 The MIT License

 Copyright (c) 2010-2014 Paul R. Holser, Jr.

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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Collections.*;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.generator.Generator;
import org.javaruntype.type.Types;

import static com.pholser.junit.quickcheck.internal.Reflection.*;

public class ParameterContext {
    private static final String EXPLICIT_GENERATOR_TYPE_MISMATCH_MESSAGE =
        "The generator %s named in @%s on parameter %s does not produce a type-compatible object";

    private final String parameterName;
    private final AnnotatedType parameterType;
    private final List<Weighted<Generator<?>>> explicits = new ArrayList<>();

    private int configuredSampleSize;
    private SampleSizer sampleSizer;
    private int discardRatio;
    private String constraint;

    public ParameterContext(String parameterName, AnnotatedType parameterType) {
        this.parameterName = parameterName;
        this.parameterType = parameterType;
    }

    public ParameterContext annotate(AnnotatedElement element) {
        ForAll quantifier = element.getAnnotation(ForAll.class);
        addQuantifier(quantifier);
        addConstraint(quantifier);
        addGenerators(allAnnotationsByType(element, From.class));

        return this;
    }

    public ParameterContext addQuantifier(ForAll quantifier) {
        if (quantifier != null) {
            this.configuredSampleSize = quantifier.sampleSize();
            this.discardRatio = quantifier.discardRatio();
        }

        return this;
    }

    public ParameterContext addConstraint(ForAll quantifier) {
        if (quantifier != null && !defaultValueOf(ForAll.class, "suchThat").equals(quantifier.suchThat()))
            constraint = quantifier.suchThat();

        return this;
    }

    private ParameterContext addGenerators(List<From> generators) {
        for (From each : generators) {
            Generator<?> generator = makeGenerator(each.value());
            ensureCorrectType(generator);
            explicits.add(new Weighted<>(generator, each.frequency()));
        }

        return this;
    }

    @SuppressWarnings("unchecked")
    private Generator<?> makeGenerator(Class<? extends Generator> generatorType) {
        // for Ctor/Fields
        Constructor<? extends Generator> ctor = findConstructor(generatorType, Class.class);
        if (ctor != null)
            return instantiate(ctor, rawParameterType());

        return instantiate(generatorType);
    }

    private Class<?> rawParameterType() {
        if (type() instanceof ParameterizedType)
            return (Class<?>) ((ParameterizedType) type()).getRawType();
        return (Class<?>) type();
    }

    private void ensureCorrectType(Generator<?> generator) {
        org.javaruntype.type.Type<?> parameterTypeToken = Types.forJavaLangReflectType(type());

        for (Class<?> each : generator.types()) {
            if (!maybeWrap(parameterTypeToken.getRawClass()).isAssignableFrom(maybeWrap(each))) {
                throw new IllegalArgumentException(
                    format(
                        EXPLICIT_GENERATOR_TYPE_MISMATCH_MESSAGE,
                        each,
                        From.class.getName(),
                        parameterName));
            }
        }
    }

    public AnnotatedType annotatedType() {
        return parameterType;
    }

    public Type type() {
        return parameterType.getType();
    }

    public int sampleSize() {
        if (sampleSizer == null)
            sampleSizer = new SampleSizer(configuredSampleSize, this);

        return sampleSizer.sampleSize();
    }

    public int discardRatio() {
        return discardRatio;
    }

    public String constraint() {
        return constraint;
    }

    public List<Weighted<Generator<?>>> explicitGenerators() {
        return unmodifiableList(explicits);
    }

    public boolean annotatedWith(Class<? extends Annotation> annotationType) {
        return parameterType.getAnnotation(annotationType) != null;
    }
}

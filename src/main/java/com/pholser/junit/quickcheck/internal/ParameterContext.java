/*
 The MIT License

 Copyright (c) 2010-2012 Paul R. Holser, Jr.

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
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.SuchThat;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.internal.random.JDKSourceOfRandomness;
import org.javaruntype.type.Types;

import static java.util.Collections.*;

public class ParameterContext {
    private static final String EXPLICIT_GENERATOR_TYPE_MISMATCH_MESSAGE =
        "The generator %s named in @%s on parameter of type %s does not produce a type-compatible object";

    private final Type parameterType;
    private final String parameterName;
    private final GeneratorRepository repo = new GeneratorRepository(new JDKSourceOfRandomness());

    private int configuredSampleSize;
    private SampleSizer sampleSizer;
    private int discardRatio;
    private String constraint;
    private Map<Class<? extends Annotation>, Annotation> configurations =
        new HashMap<Class<? extends Annotation>, Annotation>();

    public ParameterContext(Type parameterType, String parameterName) {
        this.parameterType = parameterType;
        this.parameterName = parameterName;
    }

    public ParameterContext addQuantifier(ForAll quantifier) {
        this.configuredSampleSize = quantifier.sampleSize();
        this.discardRatio = quantifier.discardRatio();
        return this;
    }

    public ParameterContext addConstraint(SuchThat constraint) {
        if (constraint != null)
            this.constraint = constraint.value();

        return this;
    }

    public ParameterContext addGenerators(From generators) {
        for (Class<? extends Generator> each : generators.value()) {
            Generator<?> generator = Reflection.instantiate(each);
            ensureCorrectType(generator);
            repo.add(generator);
        }

        return this;
    }

    public ParameterContext addConfigurations(List<Annotation> configurations) {
        for (Annotation each : configurations)
            addConfiguration(each.annotationType(), each);
        return this;
    }

    public void addConfiguration(Class<? extends Annotation> annotationType, Annotation configuration) {
        this.configurations.put(annotationType, configuration);
    }

    private void ensureCorrectType(Generator<?> generator) {
        org.javaruntype.type.Type<?> parmType = Types.forJavaLangReflectType(parameterType);

        for (Class<?> each : generator.types()) {
            org.javaruntype.type.Type<?> generatorType = Types.forJavaLangReflectType(each);

            if (!parmType.isAssignableFrom(generatorType)) {
                throw new IllegalArgumentException(String.format(EXPLICIT_GENERATOR_TYPE_MISMATCH_MESSAGE, each,
                    From.class.getName(), parameterType));
            }
        }
    }

    public Type parameterType() {
        return parameterType;
    }

    public String parameterName() {
        return parameterName;
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

    public Generator<?> explicitGenerator() {
        return repo.isEmpty() ? null : repo.generatorFor(parameterType);
    }

    public Map<Class<? extends Annotation>, Annotation> configurations() {
        return unmodifiableMap(configurations);
    }
}

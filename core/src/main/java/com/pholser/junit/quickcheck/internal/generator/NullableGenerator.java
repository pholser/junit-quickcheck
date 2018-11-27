/*
 The MIT License

 Copyright (c) 2010-2018 Paul R. Holser, Jr.

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

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.Generators;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.javaruntype.type.TypeParameter;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.pholser.junit.quickcheck.internal.Reflection.defaultValueOf;

class NullableGenerator<T> extends Generator<T> {
    private final Generator<T> delegate;
    private float probabilityOfNull = (Float) defaultValueOf(NullAllowed.class, "probability");

    NullableGenerator(Generator<T> delegate) {
        super(delegate.types());
        this.delegate = delegate;
    }

    @Override
    public T generate(SourceOfRandomness random, GenerationStatus status) {
        if (random.nextFloat(0.0f, 1.0f) < probabilityOfNull) {
            return null;
        }
        else {
            return delegate.generate(random, status);
        }
    }

    private void configure(NullAllowed nullAllowed) {
        if (nullAllowed.probability() >= 0.0f && nullAllowed.probability() <= 1.0f) {
            this.probabilityOfNull = nullAllowed.probability();
        }
        else {
            throw new IllegalArgumentException("NullAllowed probability must be in the [0,1] range");
        }
    }

    @Override
    public boolean canRegisterAsType(Class<?> type) {
        return delegate.canRegisterAsType(type);
    }

    @Override
    public boolean hasComponents() {
        return delegate.hasComponents();
    }

    @Override
    public int numberOfNeededComponents() {
        return delegate.numberOfNeededComponents();
    }

    @Override
    public void addComponentGenerators(List<Generator<?>> newComponents) {
        delegate.addComponentGenerators(newComponents);
    }

    @Override
    public boolean canGenerateForParametersOfTypes(List<TypeParameter<?>> typeParameters) {
        return delegate.canGenerateForParametersOfTypes(typeParameters);
    }

    @Override
    public void configure(AnnotatedType annotatedType) {
        Optional.ofNullable(annotatedType.getAnnotation(NullAllowed.class)).ifPresent(this::configure);
        delegate.configure(annotatedType);
    }

    @Override
    public void configure(AnnotatedElement element) {
        delegate.configure(element);
    }

    @Override
    public void provide(Generators provided) {
        delegate.provide(provided);
    }

    @Override
    public boolean canShrink(Object larger) {
        return delegate.canShrink(larger);
    }

    @Override
    public List<T> doShrink(SourceOfRandomness random, T larger) {
        return delegate.doShrink(random, larger);
    }

    @Override
    public BigDecimal magnitude(Object value) {
        return delegate.magnitude(value);
    }
}

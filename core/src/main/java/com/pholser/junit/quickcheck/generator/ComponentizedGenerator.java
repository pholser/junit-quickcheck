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

package com.pholser.junit.quickcheck.generator;

import java.lang.reflect.AnnotatedType;
import java.util.ArrayList;
import java.util.List;

import org.javaruntype.type.TypeParameter;

import static java.util.Collections.*;

import static com.pholser.junit.quickcheck.internal.Reflection.*;

/**
 * Produces values for property parameters of types that have parameterizations
 * that would also need generation, such as collections, maps, and predicates.
 *
 * @param <T> type of property parameter to apply this generator's values to
 */
public abstract class ComponentizedGenerator<T> extends Generator<T> {
    private final List<Generator<?>> components = new ArrayList<>();

    /**
     * @param type class token for type of property parameter this generator
     * is applicable to
     */
    protected ComponentizedGenerator(Class<T> type) {
        super(type);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Generators of this type do not get called upon to generate values
     * for parameters of type {@link Object}.</p>
     */
    @Override public boolean canRegisterAsType(Class<?> type) {
        return !Object.class.equals(type);
    }

    @Override public final boolean hasComponents() {
        return true;
    }

    @Override public void addComponentGenerators(List<Generator<?>> newComponents) {
        components.clear();
        components.addAll(newComponents);
    }

    @Override public boolean canGenerateForParametersOfTypes(List<TypeParameter<?>> typeParameters) {
        return numberOfNeededComponents() == typeParameters.size();
    }

    @Override public void provide(Generators provided) {
        super.provide(provided);

        for (Generator<?> each : components)
            each.provide(provided);
    }

    @Override public void configure(AnnotatedType annotatedType) {
        super.configure(annotatedType);

        List<AnnotatedType> annotatedComponentTypes = annotatedComponentTypes(annotatedType);

        if (annotatedComponentTypes.size() == components.size()) {
            for (int i = 0; i < components.size(); ++i)
                components.get(i).configure(annotatedComponentTypes.get(i));
        }
    }

    /**
     * @return this generator's component generators
     */
    protected List<Generator<?>> componentGenerators() {
        return unmodifiableList(components);
    }
}

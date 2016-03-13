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
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static com.pholser.junit.quickcheck.internal.Reflection.*;

/**
 * <p>Produces instances of a class by generating values for the parameters of
 * one of the constructors on the class, and invoking the constructor.</p>
 *
 * <p>If a constructor parameter is marked with an annotation that influences
 * the generation of a given kind of value, that annotation will be applied to
 * the generation of values for that parameter.</p>
 *
 * <p>This generator is intended to be used with
 * {@link com.pholser.junit.quickcheck.From}, and not to be available via the
 * {@link java.util.ServiceLoader} mechanism.</p>
 *
 * @param <T> the type of objects generated
 */
public class Ctor<T> extends Generator<T> {
    private final Constructor<T> ctor;
    private final Parameter[] parameters;
    private final List<Generator<?>> parameterGenerators = new ArrayList<>();

    /**
     * Reflects the given type for a single accessible constructor to be used
     * to generate values of that type.
     *
     * @param type the type of objects to be generated
     */
    public Ctor(Class<T> type) {
        this(singleAccessibleConstructor(type));
    }

    /**
     * Uses the given constructor to generate values of the declaring type.
     *
     * @param ctor the constructor to reflect on to generate constructor
     * parameter values
     */
    public Ctor(Constructor<T> ctor) {
        super(ctor.getDeclaringClass());

        this.ctor = ctor;
        this.parameters = ctor.getParameters();
    }

    @Override public T generate(SourceOfRandomness random, GenerationStatus status) {
        return instantiate(ctor, arguments(random, status));
    }

    @Override public boolean canRegisterAsType(Class<?> type) {
        return false;
    }

    @Override public void provide(Generators provided) {
        super.provide(provided);

        parameterGenerators.clear();
        for (Parameter each : parameters)
            parameterGenerators.add(gen().parameter(each));
    }

    @Override public void configure(AnnotatedType annotatedType) {
        super.configure(annotatedType);

        for (int i = 0; i < parameters.length; ++i)
            parameterGenerators.get(i).configure(parameters[i].getAnnotatedType());
    }

    private Object[] arguments(SourceOfRandomness random, GenerationStatus status) {
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < args.length; ++i)
            args[i] = parameterGenerators.get(i).generate(random, status);

        return args;
    }
}

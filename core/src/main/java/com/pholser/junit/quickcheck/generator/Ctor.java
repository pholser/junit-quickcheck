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

import com.pholser.junit.quickcheck.internal.ParameterTypeContext;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static com.pholser.junit.quickcheck.internal.Reflection.*;

/**
 * <p>Produces instances of a class by reflecting the class for a single
 * accessible constructor, generating values for the constructor's parameters,
 * and invoking the constructor.</p>
 *
 * <p>If a constructor parameter is marked with an annotation that influences
 * the generation of a given kind of value, it will be applied to the
 * generation of values for that parameter.</p>
 *
 * <p>This generator is intended to be used with
 * {@link com.pholser.junit.quickcheck.From}, and not to be available via the
 * {@link java.util.ServiceLoader} mechanism.</p>
 *
 * @param <T> the type of objects generated
 */
public class Ctor<T> extends Generator<T> {
    private final Constructor<T> single;
    private final Parameter[] parameters;
    private final List<Generator<?>> parameterGenerators = new ArrayList<>();

    /**
     * @param type the type of objects to be generated
     */
    public Ctor(Class<T> type) {
        super(type);

        this.single = singleAccessibleConstructor(type);
        this.parameters = single.getParameters();
    }

    @Override public T generate(SourceOfRandomness random, GenerationStatus status) {
        return instantiate(single, arguments(random, status));
    }

    @Override public boolean canRegisterAsType(Class<?> type) {
        return false;
    }

    @Override public void provideRepository(GeneratorRepository provided) {
        super.provideRepository(provided);

        parameterGenerators.clear();
        for (Parameter each : parameters)
            parameterGenerators.add(generatorFor(parameterTypeContext(each)));
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

    private ParameterTypeContext parameterTypeContext(Parameter parameter) {
        return new ParameterTypeContext(
            parameter.getName(),
            parameter.getAnnotatedType(),
            single.getName())
            .annotate(parameter);
    }
}

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

package com.pholser.junit.quickcheck.generator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

import com.pholser.junit.quickcheck.internal.AnnotatedConstructorParameter;
import com.pholser.junit.quickcheck.internal.ParameterContext;
import com.pholser.junit.quickcheck.internal.Reflection;
import com.pholser.junit.quickcheck.internal.ReflectionException;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/**
 * <p>A generator that produces instances of a class by reflecting the class for a single accessible constructor,
 * generating values for the constructor's parameters, and invoking the constructor.</p>
 *
 * <p>If a constructor parameter is marked with an annotation that influences the generation of a given kind of
 * value, it will be applied to the generation of values for that parameter.</p>
 *
 * @param <T> the type of objects generated
 */
public class Ctor<T> extends Generator<T> {
    /**
     * @param type the type of objects to be generated
     */
    public Ctor(Class<T> type) {
        super(type);
    }

    @Override public T generate(SourceOfRandomness random, GenerationStatus status) {
        Class<T> type = types().get(0);
        Constructor<?>[] constructors = type.getConstructors();
        if (constructors.length != 1)
            throw new ReflectionException(type + " needs a single accessible constructor");

        @SuppressWarnings("unchecked")
        Constructor<T> single = (Constructor<T>) constructors[0];

        Object[] args = arguments(
            single.getGenericParameterTypes(),
            single.getParameterAnnotations(),
            random,
            status);

        return Reflection.instantiate(single, args);
    }

    @Override public boolean canRegisterAsType(Class<?> type) {
        return false;
    }

    private Object[] arguments(
        Type[] parameterTypes,
        Annotation[][] parameterAnnotations,
        SourceOfRandomness random,
        GenerationStatus status) {

        Object[] args = new Object[parameterTypes.length];

        for (int i = 0; i < args.length; ++i) {
            AnnotatedConstructorParameter ctorParameter = new AnnotatedConstructorParameter(parameterAnnotations[i]);
            ParameterContext parameter = new ParameterContext(parameterTypes[i]).annotate(ctorParameter);
            args[i] = generatorFor(parameter).generate(random, status);
        }

        return args;
    }
}

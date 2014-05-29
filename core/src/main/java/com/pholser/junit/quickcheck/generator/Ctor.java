package com.pholser.junit.quickcheck.generator;

import com.pholser.junit.quickcheck.internal.ParameterContext;
import com.pholser.junit.quickcheck.internal.Reflection;
import com.pholser.junit.quickcheck.internal.ReflectionException;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

public class Ctor<T> extends Generator<T> {
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

        Object[] arguments =
            arguments(single.getGenericParameterTypes(), single.getParameterAnnotations(), random, status);

        return Reflection.instantiateQuietly(single, arguments);
    }

    @Override public boolean canRegisterAsType(Class<?> type) {
        return false;
    }

    private Object[] arguments(Type[] parameterTypes, Annotation[][] parameterAnnotations, SourceOfRandomness random,
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

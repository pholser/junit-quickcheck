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

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.lang.System.*;
import static java.lang.reflect.Proxy.*;

import static com.pholser.junit.quickcheck.internal.Reflection.*;

/**
 * Helper class for creating instances of "functional interfaces".
 */
public final class Lambdas {
    private static final Constructor<MethodHandles.Lookup> METHOD_LOOKUP_CTOR =
        findDeclaredConstructor(MethodHandles.Lookup.class, Class.class, int.class);

    private Lambdas() {
        throw new UnsupportedOperationException();
    }

    /**
     * <p>Creates an instance of a given "functional interface" type, whose
     * single abstract method returns values of the type produced by the given
     * generator. The arguments to the lambda's single method will be used to
     * seed a random generator that will be used to generate the return value
     * of that method.</p>
     *
     * <p>junit-quickcheck uses this to create random values for property
     * parameters whose type is determined to be a
     * {@linkplain FunctionalInterface functional interface}. Custom generators
     * for functional interface types can use this also.</p>
     *
     * @param lambdaType a functional interface type token
     * @param returnValueGenerator a generator for the return type of the
     * functional interface's single method
     * @param status an object to be passed along to the generator that will
     * produce the functional interface's method return value
     * @param <T> the functional interface type token
     * @param <U> the type of the generated return value of the functional
     * interface method
     * @return an instance of the functional interface type, whose single
     * method will return a generated value
     * @throws IllegalArgumentException if {@code lambdaType} is not a
     * functional interface type
     */
    public static <T, U> T makeLambda(
        Class<T> lambdaType,
        Generator<U> returnValueGenerator,
        GenerationStatus status) {

        if (singleAbstractMethodOf(lambdaType) == null)
            throw new IllegalArgumentException(lambdaType + " is not a functional interface type");

        return lambdaType.cast(newProxyInstance(
            lambdaType.getClassLoader(),
            new Class<?>[] { lambdaType },
            new LambdaInvocationHandler<>(lambdaType, returnValueGenerator, status)));
    }

    private static class LambdaInvocationHandler<T, U> implements InvocationHandler {
        private final Class<T> lambdaType;
        private final Generator<U> returnValueGenerator;
        private final GenerationStatus status;

        LambdaInvocationHandler(
            Class<T> lambdaType,
            Generator<U> returnValueGenerator,
            GenerationStatus status) {

            this.lambdaType = lambdaType;
            this.returnValueGenerator = returnValueGenerator;
            this.status = status;
        }

        @Override public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {

            if (Object.class.equals(method.getDeclaringClass()))
                return handleObjectMethod(proxy, method, args);
            if (method.isDefault())
                return handleDefaultMethod(proxy, method, args);

            SourceOfRandomness source = new SourceOfRandomness(new Random());
            source.setSeed(Arrays.hashCode(args));
            return returnValueGenerator.generate(source, status);
        }

        private Object handleObjectMethod(Object proxy, Method method, Object[] args) {
            if ("equals".equals(method.getName()))
                return proxy == args[0];

            if ("hashCode".equals(method.getName()))
                return identityHashCode(proxy);

            return handleToString();
        }

        private String handleToString() {
            return "a randomly generated instance of " + lambdaType;
        }

        private Object handleDefaultMethod(Object proxy, Method method, Object[] args)
            throws Throwable {

            MethodHandles.Lookup lookup =
                METHOD_LOOKUP_CTOR.newInstance(
                    method.getDeclaringClass(),
                    MethodHandles.Lookup.PRIVATE);
            return lookup.unreflectSpecial(method, method.getDeclaringClass())
                .bindTo(proxy)
                .invokeWithArguments(args);
        }
    }
}

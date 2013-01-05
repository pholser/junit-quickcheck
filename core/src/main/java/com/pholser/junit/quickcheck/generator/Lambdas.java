package com.pholser.junit.quickcheck.generator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Random;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.lang.System.*;

public class Lambdas {
    public static <T, U> T makeLambda(final Class<T> lambdaType, final Generator<U> returnValueGenerator,
                                      final GenerationStatus status) {
        final Random forLambda = new Random();

        return lambdaType.cast(Proxy.newProxyInstance(lambdaType.getClassLoader(), new Class<?>[] { lambdaType },
            new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) {
                    if (Object.class.equals(method.getDeclaringClass()))
                        return handleObjectMethod(proxy, method, args);

                    forLambda.setSeed(Arrays.hashCode(args));
                    return returnValueGenerator.generate(new SourceOfRandomness(forLambda), status);
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
            }));
    }

    private Lambdas() {
        throw new UnsupportedOperationException();
    }
}

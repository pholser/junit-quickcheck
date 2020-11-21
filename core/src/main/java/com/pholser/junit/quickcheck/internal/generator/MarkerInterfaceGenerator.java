/*
 The MIT License

 Copyright (c) 2010-2020 Paul R. Holser, Jr.

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

import static java.lang.System.identityHashCode;
import static java.lang.reflect.Proxy.newProxyInstance;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.DefaultMethodHandleMaker;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MarkerInterfaceGenerator<T> extends Generator<T> {
    private final Class<T> markerType;

    MarkerInterfaceGenerator(Class<T> markerType) {
        super(markerType);

        this.markerType = markerType;
    }

    @Override public T generate(
        SourceOfRandomness random,
        GenerationStatus status) {

        return markerType.cast(
            newProxyInstance(
                markerType.getClassLoader(),
                new Class<?>[] { markerType },
                new MarkerInvocationHandler<>(markerType)));
    }

    private static class MarkerInvocationHandler<T>
        implements InvocationHandler {

        private final Class<T> markerType;
        private final DefaultMethodHandleMaker methodHandleMaker =
            new DefaultMethodHandleMaker();

        MarkerInvocationHandler(Class<T> markerType) {
            this.markerType = markerType;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {

            if (Object.class.equals(method.getDeclaringClass()))
                return handleObjectMethod(proxy, method, args);
            if (method.isDefault())
                return handleDefaultMethod(proxy, method, args);

            return null;
        }

        private Object handleObjectMethod(
            Object proxy,
            Method method,
            Object[] args) {

            if ("equals".equals(method.getName()))
                return proxy == args[0];
            if ("hashCode".equals(method.getName()))
                return identityHashCode(proxy);

            return handleToString();
        }

        private Object handleDefaultMethod(
            Object proxy,
            Method method,
            Object[] args)
            throws Throwable {

            MethodHandle handle =
                methodHandleMaker.handleForSpecialMethod(method);
            return handle.bindTo(proxy).invokeWithArguments(args);
        }

        private String handleToString() {
            return "a synthetic instance of " + markerType;
        }
    }
}

package com.pholser.junit.quickcheck.internal;

import static com.pholser.junit.quickcheck.internal.Reflection.findDeclaredConstructor;
import static com.pholser.junit.quickcheck.internal.Reflection.jdk9OrBetter;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public final class DefaultMethodHandleMaker {
    private static volatile Constructor<Lookup> methodLookupCtorJDK8;

    private static Constructor<Lookup> methodLookupCtorJDK8() {
        if (methodLookupCtorJDK8 == null) {
            methodLookupCtorJDK8 =
                findDeclaredConstructor(Lookup.class, Class.class, int.class);
        }

        return methodLookupCtorJDK8;
    }

    public MethodHandle handleForSpecialMethod(Method method)
        throws Exception {

        return jdk9OrBetter()
            ? jdk9OrBetterMethodHandle(method)
            : jdk8MethodHandleForDefault(method);
    }

    private MethodHandle jdk9OrBetterMethodHandle(Method method)
        throws Exception {

        return MethodHandles.lookup()
            .findSpecial(
                method.getDeclaringClass(),
                method.getName(),
                MethodType.methodType(
                    method.getReturnType(),
                    method.getParameterTypes()),
                method.getDeclaringClass());
    }

    private MethodHandle jdk8MethodHandleForDefault(Method method)
        throws Exception {

        Lookup lookup =
            methodLookupCtorJDK8().newInstance(
                method.getDeclaringClass(),
                Lookup.PRIVATE);
        return lookup.unreflectSpecial(method, method.getDeclaringClass());
    }
}

/*
 The MIT License

 Copyright (c) 2010-2012 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sun.source.tree.ModifiersTree;
import org.javaruntype.type.Type;

public final class Reflection {
    private Reflection() {
        throw new UnsupportedOperationException();
    }

    public static <T> T instantiate(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception ex) {
            throw reflectionException(ex);
        }
    }

    public static Set<Type<?>> supertypes(Type<?> bottom) {
        Set<Type<?>> supertypes = new HashSet<Type<?>>();
        supertypes.add(bottom);
        supertypes.addAll(bottom.getAllTypesAssignableFromThis());
        return supertypes;
    }

    public static Object defaultValueOf(Class<? extends Annotation> annotationType, String attribute) {
        try {
            return annotationType.getMethod(attribute).getDefaultValue();
        } catch (Exception ex) {
            throw reflectionException(ex);
        }
    }

    public static List<Annotation> markedAnnotations(List<Annotation> annotations,
                                                     Class<? extends Annotation> marker) {
        List<Annotation> marked = new ArrayList<Annotation>();

        for (Annotation each : annotations) {
            if (markedWith(each, marker))
                marked.add(each);
        }

        return marked;
    }

    private static boolean markedWith(Annotation a, Class<? extends Annotation> marker) {
        for (Annotation each : a.annotationType().getAnnotations()) {
            if (each.annotationType().equals(marker))
                return true;
        }

        return false;
    }

    public static Method findMethodQuietly(Class<?> target, String methodName, Class<?>... argTypes) {
        try {
            return target.getMethod(methodName, argTypes);
        } catch (Exception ex) {
            throw reflectionException(ex);
        }
    }

    public static Object invokeQuietly(Method method, Object target, Object... args) {
        try {
            return method.invoke(target, args);
        } catch (Exception ex) {
            throw reflectionException(ex);
        }
    }

    public static Method singleAbstractMethodOf(Class<?> rawClass) {
        if (!rawClass.isInterface())
            return null;

        int abstractCount = 0;
        Method singleAbstractMethod = null;
        for (Method each : rawClass.getMethods()) {
            if (Modifier.isAbstract(each.getModifiers()) && !overridesJavaLangObjectMethod(each)) {
                singleAbstractMethod = each;
                ++abstractCount;
            }
        }

        return abstractCount == 1 ? singleAbstractMethod : null;
    }

    private static boolean overridesJavaLangObjectMethod(Method method) {
        return isEquals(method) || isHashCode(method) || isToString(method);
    }

    private static boolean isEquals(Method method) {
        return "equals".equals(method.getName())
            && method.getParameterTypes().length == 1
            && Object.class.equals(method.getParameterTypes()[0]);
    }

    private static boolean isHashCode(Method method) {
        return "hashCode".equals(method.getName())
            && method.getParameterTypes().length == 0;
    }

    private static boolean isToString(Method method) {
        return "toString".equals(method.getName())
            && method.getParameterTypes().length == 0;
    }

    private static RuntimeException reflectionException(Exception ex) {
        if (ex instanceof InvocationTargetException)
            return new ReflectionException(((InvocationTargetException) ex).getTargetException());
        if (ex instanceof RuntimeException)
            return (RuntimeException) ex;

        return new ReflectionException(ex);
    }
}

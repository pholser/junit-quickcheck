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

package com.pholser.junit.quickcheck.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.javaruntype.type.Type;

public final class Reflection {
    private static final Map<Class<?>, Class<?>> PRIMITIVES = new HashMap<Class<?>, Class<?>>(16);

    static {
        PRIMITIVES.put(Boolean.TYPE, Boolean.class);
        PRIMITIVES.put(Byte.TYPE, Byte.class);
        PRIMITIVES.put(Character.TYPE, Character.class);
        PRIMITIVES.put(Double.TYPE, Double.class);
        PRIMITIVES.put(Float.TYPE, Float.class);
        PRIMITIVES.put(Integer.TYPE, Integer.class);
        PRIMITIVES.put(Long.TYPE, Long.class);
        PRIMITIVES.put(Short.TYPE, Short.class);
    }

    private Reflection() {
        throw new UnsupportedOperationException();
    }

    public static Class<?> maybeWrap(Class<?> clazz) {
        Class<?> wrapped = PRIMITIVES.get(clazz);
        return wrapped == null ? clazz : wrapped;
    }

    public static <T> Constructor<T> findConstructor(Class<T> type, Class<?> parameterTypes) {
        try {
            return type.getConstructor(parameterTypes);
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> singleAccessibleConstructor(Class<T> type) {
        Constructor<?>[] constructors = type.getConstructors();
        if (constructors.length != 1)
            throw new ReflectionException(type + " needs a single accessible constructor");

        return (Constructor<T>) constructors[0];
    }

    public static <T> T instantiate(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception ex) {
            throw reflectionException(ex);
        }
    }

    public static <T> T instantiate(Constructor<T> ctor, Object... args) {
        try {
            return ctor.newInstance(args);
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

    public static List<Annotation> markedAnnotations(
        List<Annotation> annotations,
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

    public static Method findMethod(Class<?> target, String methodName, Class<?>... argTypes) {
        try {
            return target.getMethod(methodName, argTypes);
        } catch (Exception ex) {
            throw reflectionException(ex);
        }
    }

    public static Object invoke(Method method, Object target, Object... args) {
        try {
            return method.invoke(target, args);
        } catch (Exception ex) {
            throw reflectionException(ex);
        }
    }

    public static List<Field> allDeclaredFieldsOf(Class<?> type) {
        List<Field> allFields = new ArrayList<Field>();

        for (Class<?> c = type; c != null; c = c.getSuperclass())
            Collections.addAll(allFields, c.getDeclaredFields());

        return allFields;
    }

    public static void setField(
        final Field field,
        Object target,
        Object value,
        final boolean suppressProtection) {

        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            @Override public Void run() {
                field.setAccessible(suppressProtection);
                return null;
            }
        });

        try {
            field.set(target, value);
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

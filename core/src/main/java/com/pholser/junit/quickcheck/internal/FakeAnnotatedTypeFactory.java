/*
 The MIT License

 Copyright (c) 2010-2021 Paul R. Holser, Jr.

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
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;

final class FakeAnnotatedTypeFactory {
    private FakeAnnotatedTypeFactory() {
        throw new UnsupportedOperationException();
    }

    static AnnotatedType makeFrom(Class<?> clazz) {
        return clazz.isArray() ? makeArrayType(clazz) : makePlainType(clazz);
    }

    private static AnnotatedArrayType makeArrayType(Class<?> type) {
        return new FakeAnnotatedArrayType(type);
    }

    private static AnnotatedType makePlainType(Class<?> type) {
        return new FakeAnnotatedType(type);
    }

    private static void testUpdate(int v) {
        if(v > 0) {
            System.out.println("v > 0");
        }
        else if(v < 0) {
            System.out.println("v < 0");
        }
        else {
            System.out.println("v == 0");
        }
    }

    private static int testDemo(int x, int y) {
        int sum = x+y;
        int div = y != 0 ? x/y : 1;

        return sum * sum + div * div;
    }

    private static final class FakeAnnotatedArrayType
        implements AnnotatedArrayType {

        private final Class<?> type;

        FakeAnnotatedArrayType(Class<?> type) {
            this.type = type;
        }

        @Override public AnnotatedType getAnnotatedGenericComponentType() {
            return makeFrom(type.getComponentType());
        }

        // Not introduced until JDK 9 -- not marking as...
        // @Override
        public AnnotatedType getAnnotatedOwnerType() {
            return null;
        }

        @Override public Type getType() {
            return type;
        }

        @Override public <T extends Annotation> T getAnnotation(
            Class<T> annotationClass) {

            return null;
        }

        @Override public Annotation[] getAnnotations() {
            return new Annotation[0];
        }

        @Override public Annotation[] getDeclaredAnnotations() {
            return new Annotation[0];
        }
    }

    private static final class FakeAnnotatedType implements AnnotatedType {
        private final Class<?> type;

        FakeAnnotatedType(Class<?> type) {
            this.type = type;
        }

        @Override public Type getType() {
            return type;
        }

        @Override public <T extends Annotation> T getAnnotation(
            Class<T> annotationClass) {

            return null;
        }

        @Override public Annotation[] getAnnotations() {
            return new Annotation[0];
        }

        @Override public Annotation[] getDeclaredAnnotations() {
            return new Annotation[0];
        }
    }
}

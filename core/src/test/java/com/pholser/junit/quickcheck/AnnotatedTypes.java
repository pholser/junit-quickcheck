package com.pholser.junit.quickcheck;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;

public class AnnotatedTypes {
    private AnnotatedTypes() {
        throw new UnsupportedOperationException();
    }

    public static AnnotatedType from(Type type) {
        return new AnnotatedType() {
            @Override
            public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
                return null;
            }

            @Override
            public Annotation[] getAnnotations() {
                return new Annotation[0];
            }

            @Override
            public Annotation[] getDeclaredAnnotations() {
                return new Annotation[0];
            }

            @Override
            public Type getType() {
                return type;
            }
        };
    }
}

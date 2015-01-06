package com.pholser.junit.quickcheck;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;

public class AnnotatedTypes {
    private AnnotatedTypes() {
        throw new UnsupportedOperationException();
    }

    public static AnnotatedType from(Type type, Annotation... annotations) {
        return new AnnotatedType() {
            @Override
            public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
                for (Annotation each : annotations) {
                    if (annotationClass.isAssignableFrom(each.getClass()))
                        return annotationClass.cast(each);
                }
                return null;
            }

            @Override
            public Annotation[] getAnnotations() {
                return annotations;
            }

            @Override
            public Annotation[] getDeclaredAnnotations() {
                return annotations;
            }

            @Override
            public Type getType() {
                return type;
            }
        };
    }
}

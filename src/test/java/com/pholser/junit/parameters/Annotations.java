package com.pholser.junit.parameters;

import java.lang.annotation.Annotation;

public class Annotations {
    static {
        new Annotations();
    }

    private Annotations() {
        // empty on purpose
    }

    public static Object defaultValueFor(Class<? extends Annotation> clazz, String methodName) throws Exception {
        return clazz.getMethod(methodName).getDefaultValue();
    }
}

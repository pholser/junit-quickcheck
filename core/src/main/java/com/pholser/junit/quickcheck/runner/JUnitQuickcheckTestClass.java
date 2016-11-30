package com.pholser.junit.quickcheck.runner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.internal.MethodSorter;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import static java.util.stream.Collectors.*;

class JUnitQuickcheckTestClass extends TestClass {
    JUnitQuickcheckTestClass(Class<?> testClass) {
        super(testClass);
    }

    @Override
    protected void scanAnnotatedMembers(
        Map<Class<? extends Annotation>, List<FrameworkMethod>> methodsForAnnotations,
        Map<Class<? extends Annotation>, List<FrameworkField>> fieldsForAnnotations) {

        super.scanAnnotatedMembers(methodsForAnnotations, fieldsForAnnotations);

        addDefaultInterfaceMethods(methodsForAnnotations);
    }

    private void addDefaultInterfaceMethods(
        Map<Class<? extends Annotation>, List<FrameworkMethod>> methodsForAnnotations) {

        for (Class<?> each : implementedInterfaces()) {
            addDefaultInterfaceMethodsFrom(methodsForAnnotations, each);
        }
    }

    private void addDefaultInterfaceMethodsFrom(
        Map<Class<? extends Annotation>, List<FrameworkMethod>> methodsForAnnotations,
        Class<?> iface) {

        List<Method> defaultMethods =
            Arrays.stream(MethodSorter.getDeclaredMethods(iface))
                .filter(Method::isDefault)
                .collect(toList());

        for (Method each : defaultMethods) {
            addToAnnotationLists(
                new FrameworkMethod(each),
                methodsForAnnotations);
        }
    }

    private Set<Class<?>> implementedInterfaces() {
        Set<Class<?>> interfaces = new HashSet<>();

        for (Class<?> c = getJavaClass(); c != null; c = c.getSuperclass())
            Collections.addAll(interfaces, c.getInterfaces());

        return interfaces;
    }
}

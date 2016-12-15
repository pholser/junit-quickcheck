package com.pholser.junit.quickcheck.iface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runners.model.JUnitQuickcheckTestClass;

public class Interfaces {
    public interface A_1_1 {}
    public interface A_2_1 {}
    public interface A_2_2 {}
    public interface A_3_1 {}

    public interface A_1 extends A_1_1 {}
    public interface A_2 extends A_2_1, A_2_2 {}
    public interface A_3 extends A_3_1 {}

    public interface B_1_1 {}
    public interface B_1_2 {}
    public interface B_2_1 {}
    public interface B_3_1 {}
    public interface B_3_2 {}
    public interface B_4_1 {}

    public interface B_1 extends B_1_1, B_1_2 {}
    public interface B_2 extends B_2_1 {}
    public interface B_3 extends B_3_1, B_3_2 {}
    public interface B_4 extends B_4_1 {}

    public interface C_1_root {}
    public interface C_1_1 extends C_1_root {}
    public interface C_1_2 extends C_1_root {}
    public interface C_1_3 extends C_1_root {}

    public interface C_1 extends C_1_1, C_1_2, C_1_3 {}
    public interface C_2 {}

    public interface D_1 {}
    public interface D_2_1 {}
    public interface D_2 extends D_2_1 {}

    public interface A extends A_1, A_2, A_3 {}
    public interface B extends B_1, B_2, B_3, B_4 {}
    public interface C extends B, C_1, C_2 {}
    public interface D extends A, D_1, D_2 {}

    public static class Root implements A {}
    public static class Internal extends Root implements B, D {}
    public static class Leaf extends Internal implements A, B, C, D {}

    @Test public void interfaceAncestryTest() {
        interfaceAncestry(Leaf.class)
            .map(Class::getSimpleName)
            .forEachOrdered(System.out::println);
    }

    @Test public void reverseInterfaceAncestryTest() {
        reverseInterfaceAncestry(Leaf.class)
            .map(Class::getSimpleName)
            .forEachOrdered(System.out::println);
    }

    @Test public void superclassHierarchyPlusInterfaceAncestry() {
        superclassHierarchy(Leaf.class)
            .flatMap(Interfaces::interfaceAncestry)
            .map(Class::getSimpleName)
            .forEachOrdered(System.out::println);
    }

    @Test public void reverseSuperclassHierarchyPlusReverseInterfaceAncestry() {
        reverseSuperclassHierarchy(Leaf.class)
            .flatMap(Interfaces::reverseInterfaceAncestry)
            .map(Class::getSimpleName)
            .forEachOrdered(System.out::println);
    }

    private Stream<Class<?>> superclassHierarchy(Class<?> clazz) {
        List<Class<?>> hierarchy = new ArrayList<>();
        for (Class<?> c = clazz; c != null; c = c.getSuperclass())
            hierarchy.add(c);
        return hierarchy.stream();
    }

    private Stream<Class<?>> reverseSuperclassHierarchy(Class<?> clazz) {
        List<Class<?>> hierarchy = new ArrayList<>();
        for (Class<?> c = clazz; c != null; c = c.getSuperclass())
            hierarchy.add(c);
        Collections.reverse(hierarchy);
        return hierarchy.stream();
    }

    private static Stream<Class<?>> interfaceAncestry(Class<?> c) {
        return Stream.concat(
            Stream.of(c),
            Arrays.stream(c.getInterfaces())
                .flatMap(Interfaces::interfaceAncestry));
    }

    private static Stream<Class<?>> reverseInterfaceAncestry(Class<?> c) {
        return Stream.concat(
            Arrays.stream(c.getInterfaces())
                .flatMap(Interfaces::reverseInterfaceAncestry),
            Stream.of(c));
    }
}

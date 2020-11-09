/*
 The MIT License

 Copyright (c) 2004-2011 Paul R. Holser, Jr.

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

import static com.pholser.junit.quickcheck.internal.Reflection.allAnnotations;
import static com.pholser.junit.quickcheck.internal.Reflection.allDeclaredFieldsOf;
import static com.pholser.junit.quickcheck.internal.Reflection.defaultValueOf;
import static com.pholser.junit.quickcheck.internal.Reflection.findConstructor;
import static com.pholser.junit.quickcheck.internal.Reflection.findDeclaredConstructor;
import static com.pholser.junit.quickcheck.internal.Reflection.findField;
import static com.pholser.junit.quickcheck.internal.Reflection.findMethod;
import static com.pholser.junit.quickcheck.internal.Reflection.instantiate;
import static com.pholser.junit.quickcheck.internal.Reflection.invoke;
import static com.pholser.junit.quickcheck.internal.Reflection.isMarkerInterface;
import static com.pholser.junit.quickcheck.internal.Reflection.setField;
import static com.pholser.junit.quickcheck.internal.Reflection.singleAbstractMethodOf;
import static com.pholser.junit.quickcheck.internal.Reflection.singleAccessibleConstructor;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import org.junit.Test;

public class ReflectionTest {
    @Test public void findingConstructor() {
        Constructor<Integer> ctor = findConstructor(Integer.class, int.class);

        assertEquals(int.class, ctor.getParameterTypes()[0]);
    }

    @Test public void findingConstructorWhenNoSuchConstructor() {
        assertThrows(
            ReflectionException.class,
            () -> findConstructor(Integer.class, Object.class));
    }

    @Test public void findingDeclaredConstructor() {
        Constructor<Integer> ctor =
            findDeclaredConstructor(Integer.class, int.class);

        assertEquals(int.class, ctor.getParameterTypes()[0]);
    }

    @Test public void findingDeclaredConstructorWhenNoSuchConstructor() {
        assertThrows(
            ReflectionException.class,
            () -> findDeclaredConstructor(Integer.class, Object.class));
    }

    @Test public void findingSingleAccessibleConstructorSuccessfully() {
        Constructor<Object> ctor = singleAccessibleConstructor(Object.class);

        assertEquals(0, ctor.getParameterTypes().length);
    }

    @Test public void
    rejectsFindingSingleAccessibleConstructorOnNonConformingClass() {
        ReflectionException ex =
            assertThrows(
                ReflectionException.class,
                () -> singleAccessibleConstructor(Integer.class));
        assertEquals(
            Integer.class + " needs a single accessible constructor",
            ex.getMessage());
    }

    @Test public void invokingZeroArgConstructorWrapsInstantiationException() {
        ReflectionException ex =
            assertThrows(
                ReflectionException.class,
                () -> instantiate(ZeroArgInstantiationProblematic.class));
        assertThat(
            ex.getMessage(),
            containsString(InstantiationException.class.getName()));
    }

    @Test public void invokingZeroArgConstructorWrapsIllegalAccessException() {
        ReflectionException ex =
            assertThrows(
                ReflectionException.class,
                () -> instantiate(ZeroArgIllegalAccessProblematic.class));
        assertThat(
            ex.getMessage(),
            containsString(IllegalAccessException.class.getName()));
    }

    @Test public void
    invokingZeroArgConstructorPropagatesExceptionsRaisedByConstructor() {
        assertThrows(
            IllegalStateException.class,
            () -> instantiate(InvocationTargetProblematic.class));
    }

    private abstract static class MultiArgInstantiationProblematic {
        public MultiArgInstantiationProblematic(int i) {
            // no-op
        }
    }

    @Test public void
    invokingNonZeroArgConstructorQuietlyWrapsInstantiationException() {
        ReflectionException ex =
            assertThrows(
                ReflectionException.class,
                () -> instantiate(
                    MultiArgInstantiationProblematic.class
                        .getConstructor(int.class),
                    2));
        assertThat(
            ex.getMessage(),
            containsString(InstantiationException.class.getName()));
    }

    @Test public void
    invokingNonZeroArgConstructorQuietlyWrapsIllegalAccessException() {
        ReflectionException ex =
            assertThrows(
                ReflectionException.class,
                () -> instantiate(
                    MultiArgIllegalAccessProblematic.class
                        .getDeclaredConstructor(int.class),
                    2));
        assertThat(
            ex.getMessage(),
            containsString(IllegalAccessException.class.getName()));
    }

    @Test public void
    invokingNonZeroArgConstructorQuietlyPropagatesIllegalArgumentException() {
        assertThrows(
            IllegalArgumentException.class,
            () -> instantiate(
                Integer.class.getDeclaredConstructor(int.class),
                "2"));
    }

    @Test public void
    invokingNonZeroArgConstructorWrapsExceptionsRaisedByConstructor() {
        ReflectionException ex =
            assertThrows(
                ReflectionException.class,
                () -> instantiate(
                    InvocationTargetProblematic.class.getConstructor(
                        int.class),
                    2));
        assertThat(
            ex.getMessage(),
            containsString(IndexOutOfBoundsException.class.getName()));
    }

    private abstract static class ZeroArgInstantiationProblematic {
        protected ZeroArgInstantiationProblematic() {
            // no-op
        }
    }

    @Test public void
    findingNonExistentMethodQuietlyWrapsNoSuchMethodException() {
        ReflectionException ex =
            assertThrows(
                ReflectionException.class,
                () -> findMethod(getClass(), "foo"));
        assertThat(
            ex.getMessage(),
            containsString(NoSuchMethodException.class.getName()));
    }

    @Test public void invokingMethodQuietlyWrapsIllegalAccessException()
        throws Exception {

        Method method =
            ZeroArgIllegalAccessProblematic.class.getDeclaredMethod("foo");

        ReflectionException ex =
            assertThrows(
                ReflectionException.class,
                () -> invoke(method, new ZeroArgIllegalAccessProblematic(0)));
        assertThat(
            ex.getMessage(),
            containsString(IllegalAccessException.class.getName()));
    }

    @Test public void
    invokingMethodQuietlyPropagatesExceptionRaisedByMethod() {
        ReflectionException ex =
            assertThrows(
                ReflectionException.class,
                () -> invoke(findMethod(getClass(), "bar"), this));
        assertThat(
            ex.getMessage(),
            containsString(NumberFormatException.class.getName()));
    }

    @Test public void invokingMethodPropagatesIllegalArgumentException() {
        assertThrows(
            IllegalArgumentException.class,
            () -> invoke(findMethod(getClass(), "bar"), this, "baz"));
    }

    @Test public void invokingMethodQuietlyPropagatesOtherRuntimeExceptions() {
        assertThrows(
            NullPointerException.class,
            () -> invoke(findMethod(getClass(), "bar"), null));
    }

    public void bar() {
        throw new NumberFormatException();
    }

    @Test public void findingDefaultValueOfAnnotationAttribute() {
        assertEquals("baz", defaultValueOf(Foo.class, "bar"));
    }

    @Test public void missingAnnotationAttribute() {
        ReflectionException ex =
            assertThrows(
                ReflectionException.class,
                () -> defaultValueOf(Foo.class, "noneSuch"));
        assertThat(
            ex.getMessage(),
            containsString(NoSuchMethodException.class.getName()));
    }

    public @interface Foo {
        String bar() default "baz";
    }

    @Test public void aClassIsNotASingleAbstractMethodType() {
        assertNull(singleAbstractMethodOf(String.class));
    }

    @Test public void
    anInterfaceWithNoMethodsIsNotASingleAbstractMethodType() {
        assertNull(singleAbstractMethodOf(Serializable.class));
    }

    @Test public void
    anInterfaceWithASingleAbstractMethodIsASingleAbstractMethodType()
    throws Exception {
        assertEquals(
            Comparator.class.getMethod("compare", Object.class, Object.class),
            singleAbstractMethodOf(Comparator.class));
    }

    @Test public void
    anInterfaceThatOverridesEqualsIsNotASingleAbstractMethodType() {
        assertNull(singleAbstractMethodOf(OverridingEquals.class));
    }

    interface OverridingEquals {
        boolean equals(Object o);
    }

    @Test public void
    anInterfaceThatOverloadsEqualsCanBeASingleAbstractMethodType()
    throws Exception {
        assertEquals(
            OverloadingEquals.class.getMethod("equals", String.class),
            singleAbstractMethodOf(OverloadingEquals.class));
    }

    interface OverloadingEquals {
        boolean equals(String s);
    }

    @Test public void
    anInterfaceThatOverloadsEqualsWithMoreThanOneParameterCanBeASingleAbstractMethodType()
    throws Exception {

        assertEquals(
            OverloadingEqualsWithMoreParameters.class.getMethod(
                "equals",
                Object.class,
                Object.class),
            singleAbstractMethodOf(
                OverloadingEqualsWithMoreParameters.class));
    }

    interface OverloadingEqualsWithMoreParameters {
        boolean equals(Object first, Object second);
    }

    @Test public void
    anInterfaceThatOverloadsEqualsWithMixedParameterTypesIsNotASingleAbstractMethodType()
    throws Exception {
        assertEquals(
            OverloadingEqualsWithMixedParameterTypes.class.getMethod(
                "equals",
                Object.class,
                String.class,
                int.class),
            singleAbstractMethodOf(
                OverloadingEqualsWithMixedParameterTypes.class));
    }

    interface OverloadingEqualsWithMixedParameterTypes {
        boolean equals(Object first, String second, int third);
    }

    @Test public void
    anInterfaceThatOverridesHashCodeIsNotASingleAbstractMethodType() {
        assertNull(singleAbstractMethodOf(OverridingHashCode.class));
    }

    interface OverridingHashCode {
        int hashCode();
    }

    @Test public void
    anInterfaceThatOverloadsHashCodeCanBeASingleAbstractMethodType()
    throws Exception {
        assertEquals(
            OverloadingHashCode.class.getMethod("hashCode", Object.class),
            singleAbstractMethodOf(OverloadingHashCode.class));
    }

    interface OverloadingHashCode {
        int hashCode(Object o);
    }

    @Test public void
    anInterfaceThatOverridesToStringIsNotASingleAbstractMethodType() {
        assertNull(singleAbstractMethodOf(OverridingToString.class));
    }

    interface OverridingToString {
        String toString();
    }

    @Test public void
    anInterfaceThatOverloadsToStringCanBeASingleAbstractMethodType()
    throws Exception {
        assertEquals(
            OverloadingToString.class.getMethod("toString", Object.class),
            singleAbstractMethodOf(OverloadingToString.class));
    }

    interface OverloadingToString {
        String toString(Object o);
    }

    @Test
    public void aClassIsNotAMarkerInterface() {
        assertFalse(isMarkerInterface(Object.class));
    }

    @Test
    public void anInterfaceWithAMethodIsNotAMarkerInterface() {
        assertFalse(isMarkerInterface(NotAMarker.class));
    }

    public interface NotAMarker {
        String foo();
    }

    @Test
    public void
    anInterfaceWithNoMethodsButSuperMethodsIsNotAMarkerInterface() {
        assertFalse(isMarkerInterface(SubNotAMarker.class));
    }

    public interface SubNotAMarker extends NotAMarker {
    }

    @Test public void anInterfaceThatOverridesEqualsIsAMarkerInterface() {
        assertTrue(isMarkerInterface(OverridingEquals.class));
    }

    @Test public void anInterfaceThatOverloadsEqualsIsNotAMarkerInterface() {
        assertFalse(isMarkerInterface(OverloadingEquals.class));
    }

    @Test public void
    anInterfaceThatOverloadsEqualsWithMoreThanOneParameterIsNotAMarkerInterface() {
        assertFalse(
            isMarkerInterface(OverloadingEqualsWithMoreParameters.class));
    }

    @Test public void
    anInterfaceThatOverloadsEqualsWithMixedParameterTypesIsNotAMarkerInterface() {
        assertFalse(
            isMarkerInterface(OverloadingEqualsWithMixedParameterTypes.class));
    }

    @Test public void anInterfaceThatOverridesHashCodeIsAMarkerInterface() {
        assertTrue(isMarkerInterface(OverridingHashCode.class));
    }

    @Test public void anInterfaceThatOverloadsHashCodeIsNotAMarkerInterface() {
        assertFalse(isMarkerInterface(OverloadingHashCode.class));
    }

    @Test public void anInterfaceThatOverridesToStringIsAMarkerInterface() {
        assertTrue(isMarkerInterface(OverridingToString.class));
    }

    @Test public void
    anInterfaceThatOverloadsToStringIsNotAMarkerInterface() {
        assertFalse(isMarkerInterface(OverloadingToString.class));
    }

    @Test public void anInterfaceWithOnlyDefaultMethodsIsAMarkerInterface() {
        assertTrue(isMarkerInterface(OnlyDefaultMethods.class));
    }

    public interface OnlyDefaultMethods {
        default String foo() {
            return "foo";
        }
    }

    @Test public void settingFieldWithoutBypassingProtection()
        throws Exception {

        WithAccessibleField target = new WithAccessibleField();

        setField(target.getClass().getField("i"), target, 2, false);

        assertEquals(2, target.i);
    }

    public static class WithAccessibleField {
        public int i;
    }

    @Test public void settingInaccessibleFieldBypassingProtection()
        throws Exception {

        WithInaccessibleField target = new WithInaccessibleField();

        setField(target.getClass().getDeclaredField("i"), target, 3, true);

        assertEquals(3, target.i);
    }

    public static class WithInaccessibleField {
        private int i;
    }

    @Test public void settingInaccessibleFieldWithoutBypassingProtection() {
        WithInaccessibleField target = new WithInaccessibleField();

        ReflectionException ex =
            assertThrows(
                ReflectionException.class,
                () ->
                    setField(
                        target.getClass().getDeclaredField("i"),
                        target,
                        4,
                        false));

        assertThat(
            ex.getMessage(),
            containsString(IllegalAccessException.class.getName()));
    }

    @Test public void findingAllDeclaredFieldsOnAClass() throws Exception {
        List<Field> fields = allDeclaredFieldsOf(Child.class);

        assertEquals(2, fields.size());
        assertThat(fields, hasItem(Child.class.getDeclaredField("s")));
        assertThat(fields, hasItem(Parent.class.getDeclaredField("i")));
    }

    @Test public void findingField() {
        Field i = findField(Parent.class, "i");

        assertEquals(int.class, i.getType());
    }

    @Test public void findingNonExistentField() {
        ReflectionException ex =
            assertThrows(
                ReflectionException.class,
                () -> findField(Parent.class, "missing"));
        assertThat(
            ex.getMessage(),
            containsString(NoSuchFieldException.class.getName()));
    }

    public static class Parent {
        private int i;
    }

    public static class Child extends Parent {
        private String s;
    }

    @Test public void findingAllDeclaredFieldsOnAClassExcludesSynthetics()
        throws Exception {

        List<Field> fields = allDeclaredFieldsOf(Outer.Inner.class);

        assertEquals(1, fields.size());
        assertThat(fields, hasItem(Outer.Inner.class.getDeclaredField("s")));
    }

    public static class Outer {
        public class Inner {
            private String s;
        }
    }

    @Test public void findingAnnotationsRecursively() {
        Method method =
            findMethod(this.getClass(), "withMarker", String.class);

        List<Annotation> annotations =
            allAnnotations(method.getParameters()[0]);

        assertEquals(4, annotations.size());
        assertEquals(X.class, annotations.get(0).annotationType());
        assertEquals(Y.class, annotations.get(1).annotationType());
        assertTrue(Z.class.equals(annotations.get(2).annotationType()) ||
            W.class.equals(annotations.get(2).annotationType()));
        if (Z.class.equals(annotations.get(2).annotationType())) {
            assertEquals(W.class, annotations.get(3).annotationType());
        } else {
            assertEquals(Z.class, annotations.get(3).annotationType());
        }
    }

    public void withMarker(@X String s) {
    }

    @Target(PARAMETER)
    @Retention(RUNTIME)
    @Y
    public @interface X {
    }

    @Target(ANNOTATION_TYPE)
    @Retention(RUNTIME)
    @Z
    @W
    public @interface Y {
    }

    @Target(ANNOTATION_TYPE)
    @Retention(RUNTIME)
    public @interface Z {
    }

    @Target(ANNOTATION_TYPE)
    @Retention(RUNTIME)
    public @interface W {
    }
}

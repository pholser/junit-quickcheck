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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

import static com.pholser.junit.quickcheck.internal.Reflection.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.*;

public class ReflectionTest {
    @Rule public final ExpectedException thrown = none();

    @Test public void invokingConstructorQuietlyWrapsInstantiationException() {
        thrown.expect(ReflectionException.class);
        thrown.expectMessage(InstantiationException.class.getName());

        instantiate(InstantiationProblematic.class);
    }

    @Test public void invokingConstructorQuietlyWrapsIllegalAccessException() {
        thrown.expect(ReflectionException.class);
        thrown.expectMessage(IllegalAccessException.class.getName());

        instantiate(IllegalAccessProblematic.class);
    }

    @Test public void invokingConstructorPropagatesExceptionsRaisedByConstructor() {
        thrown.expect(IllegalStateException.class);

        instantiate(InvocationTargetProblematic.class);
    }

    private abstract static class InstantiationProblematic {
        protected InstantiationProblematic() {
            // no-op
        }
    }

    @Test public void findingNonExistentMethodQuietlyWrapsNoSuchMethodException() {
        thrown.expect(ReflectionException.class);
        thrown.expectMessage(NoSuchMethodException.class.getName());

        findMethodQuietly(getClass(), "foo");
    }

    @Test public void invokingMethodQuietlyWrapsIllegalAccessException() throws Exception {
        Method method = IllegalAccessProblematic.class.getDeclaredMethod("foo");

        thrown.expect(ReflectionException.class);
        thrown.expectMessage(IllegalAccessException.class.getName());

        invokeQuietly(method, new IllegalAccessProblematic(0));
    }

    @Test public void invokingMethodQuietlyPropagatesExceptionRaisedByMethod() {
        thrown.expect(ReflectionException.class);
        thrown.expectMessage(NumberFormatException.class.getName());

        invokeQuietly(findMethodQuietly(getClass(), "bar"), this);
    }

    @Test public void invokingMethodPropagatesIllegalArgumentException() {
        thrown.expect(IllegalArgumentException.class);

        invokeQuietly(findMethodQuietly(getClass(), "bar"), this, "baz");
    }

    @Test public void invokingMethodQuietlyPropagatesOtherRuntimeExceptions() throws Exception {
        thrown.expect(NullPointerException.class);

        invokeQuietly(findMethodQuietly(getClass(), "bar"), null);
    }

    public void bar() {
        throw new NumberFormatException();
    }

    @Test public void findingDefaultValueOfAnnotationAttribute() throws Exception {
        assertEquals("baz", defaultValueOf(Foo.class, "bar"));
    }

    @Test public void missingAnnotationAttribute() throws Exception {
        thrown.expect(ReflectionException.class);
        thrown.expectMessage(NoSuchMethodException.class.getName());

        defaultValueOf(Foo.class, "noneSuch");
    }

    public static @interface Foo {
        String bar() default "baz";
    }

    @Test public void aClassIsNotASingleAbstractMethodType() {
        assertNull(singleAbstractMethodOf(String.class));
    }

    @Test public void anInterfaceWithNoMethodsIsNotASingleAbstractMethodType() {
        assertNull(singleAbstractMethodOf(Serializable.class));
    }

    @Test public void anInterfaceWithASingleAbstractMethodIsASingleAbstractMethodType() throws Exception {
        assertEquals(Comparator.class.getMethod("compare", Object.class, Object.class),
            singleAbstractMethodOf(Comparator.class));
    }

    @Test public void anInterfaceThatOverridesEqualsIsNotASingleAbstractMethodType() throws Exception {
        assertNull(singleAbstractMethodOf(OverridingEquals.class));
    }

    interface OverridingEquals {
        boolean equals(Object o);
    }

    @Test public void anInterfaceThatOverloadsEqualsCanBeASingleAbstractMethodType() throws Exception {
        assertEquals(OverloadingEquals.class.getMethod("equals", String.class),
            singleAbstractMethodOf(OverloadingEquals.class));
    }

    interface OverloadingEquals {
        boolean equals(String s);
    }

    @Test public void anInterfaceThatOverloadsEqualsWithMoreThanOneParameterCanBeASingleAbstractMethodType()
        throws Exception {

        assertEquals(OverloadingEqualsWithMoreParameters.class.getMethod("equals", Object.class, Object.class),
            singleAbstractMethodOf(OverloadingEqualsWithMoreParameters.class));
    }

    interface OverloadingEqualsWithMoreParameters {
        boolean equals(Object first, Object second);
    }

    @Test public void anInterfaceThatOverloadsEqualsWithMixedParameterTypesIsNotASingleAbstractMethodType()
        throws Exception {

        assertEquals(
            OverloadingEqualsWithMixedParameterTypes.class.getMethod("equals", Object.class, String.class, int.class),
            singleAbstractMethodOf(OverloadingEqualsWithMixedParameterTypes.class));
    }

    interface OverloadingEqualsWithMixedParameterTypes {
        boolean equals(Object first, String second, int third);
    }

    @Test public void anInterfaceThatOverridesHashCodeIsNotASingleAbstractMethodType() throws Exception {
        assertNull(singleAbstractMethodOf(OverridingHashCode.class));
    }

    interface OverridingHashCode {
        int hashCode();
    }

    @Test public void anInterfaceThatOverloadsHashCodeCanBeASingleAbstractMethodType() throws Exception {
        assertEquals(OverloadingHashCode.class.getMethod("hashCode", Object.class),
            singleAbstractMethodOf(OverloadingHashCode.class));
    }

    interface OverloadingHashCode {
        int hashCode(Object o);
    }

    @Test public void anInterfaceThatOverridesToStringIsNotASingleAbstractMethodType() throws Exception {
        assertNull(singleAbstractMethodOf(OverridingToString.class));
    }

    interface OverridingToString {
        String toString();
    }

    @Test public void anInterfaceThatOverloadsToStringCanBeASingleAbstractMethodType() throws Exception {
        assertEquals(OverloadingToString.class.getMethod("toString", Object.class),
            singleAbstractMethodOf(OverloadingToString.class));
    }

    interface OverloadingToString {
        String toString(Object o);
    }

    @Test public void settingFieldWithoutBypassingProtection() throws Exception {
        WithAccessibleField target = new WithAccessibleField();

        setField(target.getClass().getField("i"), target, 2, false);

        assertEquals(2, target.i);
    }

    public static class WithAccessibleField {
        public int i;
    }

    @Test public void settingInaccessibleFieldBypassingProtection() throws Exception {
        WithInaccessibleField target = new WithInaccessibleField();

        setField(target.getClass().getDeclaredField("i"), target, 3, true);

        assertEquals(3, target.i);
    }

    public static class WithInaccessibleField {
        private int i;
    }

    @Test public void settingInaccessibleFieldWithoutBypassingProtection() throws Exception {
        WithInaccessibleField target = new WithInaccessibleField();

        thrown.expect(ReflectionException.class);
        thrown.expectMessage(IllegalAccessException.class.getName());

        setField(target.getClass().getDeclaredField("i"), target, 4, false);
    }

    @Test public void findingAllDeclaredFieldsOnAClass() throws Exception {
        List<Field> fields = allDeclaredFieldsOf(Child.class);

        assertEquals(2, fields.size());
        assertThat(fields, hasItem(Child.class.getDeclaredField("s")));
        assertThat(fields, hasItem(Parent.class.getDeclaredField("i")));
    }

    public static class Parent {
        private int i;
    }

    public static class Child extends Parent {
        private String s;
    }
}

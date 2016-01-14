/*
 The MIT License

 Copyright (c) 2010-2016 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck;

import java.util.Arrays;

import com.pholser.junit.quickcheck.internal.Zilch;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.Box;
import com.pholser.junit.quickcheck.test.generator.Foo;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

public class GenericArrayPropertyParameterTypesTest {
    @Test public void arrayOfBoxOfHuh() {
        assertThat(testResult(ArrayOfBoxOfHuh.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ArrayOfBoxOfHuh {
        @Property(trials = 5) public void shouldHold(Box<?>[] items) {
        }
    }

    @Test public void arrayOfBoxOfZilch() {
        assertThat(testResult(ArrayOfBoxOfZilch.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ArrayOfBoxOfZilch {
        @Property(trials = 5) public void shouldHold(Box<Zilch>[] items) {
        }
    }

    @Test public void arrayOfBoxOfSuperZilch() {
        assertThat(testResult(ArrayOfBoxOfSuperZilch.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ArrayOfBoxOfSuperZilch {
        @Property(trials = 5) public void shouldHold(Box<? super Zilch>[] items) {
        }
    }

    @Test public void arrayOfUnresolvedType() {
        assertThat(testResult(ArrayOfUnresolvedType.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ArrayOfUnresolvedType {
        @Property(trials = 5) public <T> void shouldHold(T[] items) {
            System.out.println(Arrays.asList(items));
        }
    }

    @Test public void arrayOfBoundedUnresolvedType() {
        assertThat(testResult(ArrayOfBoundedUnresolvedType.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ArrayOfBoundedUnresolvedType {
        @Property(trials = 5) public <T extends Foo> void shouldHold(T[] items) {
            assertEquals(Foo.class, items.getClass().getComponentType());
        }
    }

    @Test public void arrayOfUnresolvedParameterizedType() {
        assertThat(testResult(ArrayOfUnresolvedParameterizedType.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ArrayOfUnresolvedParameterizedType {
        @Property(trials = 5) public <T extends Foo> void shouldHold(Box<T>[] items) {
            assertEquals(Box.class, items.getClass().getComponentType());

            for (Box<T> each : items) {
                Foo f = each.contents();
            }
        }
    }

    @Test public void arrayOfUnresolvedParameterizedUpperBoundedType() {
        assertThat(testResult(ArrayOfUnresolvedParameterizedUpperBoundedType.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ArrayOfUnresolvedParameterizedUpperBoundedType {
        @Property(trials = 5) public <T extends Foo> void shouldHold(Box<? extends T>[] items) {
            assertEquals(Box.class, items.getClass().getComponentType());

            for (Box<? extends T> each : items) {
                Foo f = each.contents();
            }
        }
    }

    @Test public void arrayOfUnresolvedParameterizedLowerBoundedType() {
        assertThat(testResult(ArrayOfUnresolvedParameterizedLowerBoundedType.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ArrayOfUnresolvedParameterizedLowerBoundedType {
        @Property(trials = 5) public <T extends Foo> void shouldHold(Box<? super T>[] items) {
            assertEquals(Box.class, items.getClass().getComponentType());

            for (Box<? super T> each : items) {
                Object o = each.contents();
            }
        }
    }

    @Test public void arrayOfUnresolvedParameterizedNestedBoundedType() {
        assertThat(testResult(ArrayOfUnresolvedParameterizedNestedBoundedType.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ArrayOfUnresolvedParameterizedNestedBoundedType {
        @Property(trials = 5) public <T extends Foo, E extends T> void shouldHold(
            Box<? extends E>[] items) {

            assertEquals(Box.class, items.getClass().getComponentType());

            for (Box<? extends E> each : items) {
                Foo f = each.contents();
            }
        }
    }
}

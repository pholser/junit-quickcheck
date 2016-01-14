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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public final class Classes {
    private Classes() {
        throw new UnsupportedOperationException();
    }

    public static Matcher<Class<?>> isAssignableFrom(final Class<?> other) {
        return new TypeSafeMatcher<Class<?>>() {
            @Override protected boolean matchesSafely(Class<?> item) {
                return item.isAssignableFrom(other);
            }

            @Override public void describeTo(Description description) {
                description.appendText("a class that is assignable from ").appendValue(other);
            }
        };
    }

    public static List<Class<?>> classesOf(Iterable<?> items) {
        return FluentIterable
            .from(items)
            .transform(new Function<Object, Class<?>>() {
                @Override public Class<?> apply(Object input) {
                    return input.getClass();
                }
            }).toList();
    }

    public static Class<?> nearestCommonSuperclassOf(List<Class<?>> classes) {
        return commonSuperclassesOf(classes).get(0);
    }

    private static List<Class<?>> commonSuperclassesOf(List<Class<?>> classes) {
        Set<Class<?>> intersection = new LinkedHashSet<Class<?>>(getClassesBreadthFirst(classes.get(0)));

        if (classes.size() > 1) {
            for (Class<?> each : classes.subList(1, classes.size()))
                intersection.retainAll(getClassesBreadthFirst(each));
        }

        return new ArrayList<>(intersection);
    }

    private static Set<Class<?>> getClassesBreadthFirst(Class<?> clazz) {
        Set<Class<?>> classes = new LinkedHashSet<>();
        Set<Class<?>> nextLevel = new LinkedHashSet<>();
        nextLevel.add(clazz);

        do {
            classes.addAll(nextLevel);

            Set<Class<?>> thisLevel = new LinkedHashSet<>(nextLevel);
            nextLevel.clear();

            for (Class<?> each : thisLevel) {
                Class<?> superClass = each.getSuperclass();
                if (superClass != null && superClass != Object.class)
                    nextLevel.add(superClass);

                Collections.addAll(nextLevel, each.getInterfaces());
            }
        } while (!nextLevel.isEmpty());

        return classes;
    }
}

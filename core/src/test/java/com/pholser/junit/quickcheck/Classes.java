/*
 The MIT License

 Copyright (c) 2010-2020 Paul R. Holser, Jr.

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

import com.google.common.io.Resources;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.StreamSupport;

import static com.google.common.io.Resources.*;
import static java.util.stream.Collectors.*;

final class Classes {
    private Classes() {
        throw new UnsupportedOperationException();
    }

    static String currentMethodName() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        StackTraceElement element = trace[2];
        return element.getClassName() + "::" + element.getMethodName();
    }

    static Matcher<Class<?>> isAssignableFrom(final Class<?> other) {
        return new TypeSafeMatcher<Class<?>>() {
            @Override protected boolean matchesSafely(Class<?> item) {
                return item.isAssignableFrom(other);
            }

            @Override public void describeTo(Description description) {
                description.appendText("a class that is assignable from ").appendValue(other);
            }
        };
    }

    static List<Class<?>> classesOf(Iterable<?> items) {
        return StreamSupport.stream(items.spliterator(), false)
            .map(Object::getClass)
            .collect(toList());
    }

    static String resourceAsString(String name) throws IOException {
        return Resources.toString(getResource(name), Charset.forName("UTF-8"));
    }
}

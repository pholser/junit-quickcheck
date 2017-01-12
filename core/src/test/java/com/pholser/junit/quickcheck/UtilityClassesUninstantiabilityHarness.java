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

package com.pholser.junit.quickcheck;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static java.lang.reflect.Modifier.*;
import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.*;

public abstract class UtilityClassesUninstantiabilityHarness {
    @Rule public final ExpectedException thrown = none();

    private final Class<?> utility;

    protected UtilityClassesUninstantiabilityHarness(Class<?> utility) {
        this.utility = utility;
    }

    @Test public final void attemptToInstantiate() throws Exception {
        Constructor<?> constructor = utility.getDeclaredConstructor();
        assertTrue("need a private no-arg constructor", isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);

        thrown.expect(InvocationTargetException.class);
        thrown.expect(causeOfType(UnsupportedOperationException.class));

        constructor.newInstance();
    }

    private static Matcher<InvocationTargetException> causeOfType(final Class<? extends Throwable> type) {
        return new TypeSafeMatcher<InvocationTargetException>() {
            @Override public boolean matchesSafely(InvocationTargetException target) {
                return type.isInstance(target.getTargetException());
            }

            @Override public void describeTo(Description description) {
                description.appendText("an InvocationTargetException with target exception of ");
                description.appendValue(type);
            }
        };
    }
}


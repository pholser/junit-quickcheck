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

import static com.pholser.junit.quickcheck.internal.Reflection.*;

import org.junit.Ignore;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.pholser.junit.quickcheck.internal.Reflection.*;
import static org.junit.rules.ExpectedException.*;

public class ReflectionTest {
    @Rule public final ExpectedException thrown = none();

    @Test
    public void invokingConstructorQuietlyWrapsInstantiationException() {
        thrown.expect(ReflectionException.class);
        thrown.expectMessage(InstantiationException.class.getName());

        instantiate(InstantiationProblematic.class);
    }

    @Test
    public void invokingConstructorQuietlyWrapsIllegalAccessException() {
        thrown.expect(ReflectionException.class);
        thrown.expectMessage(IllegalAccessException.class.getName());

        instantiate(IllegalAccessProblematic.class);
    }

    @Test
    public void invokingConstructorPropagatesExceptionsRaisedByConstructor() {
        thrown.expect(IllegalStateException.class);

        instantiate(InvocationTargetProblematic.class);
    }

    private abstract static class InstantiationProblematic {
        protected InstantiationProblematic() {
            // no-op
        }
    }
}

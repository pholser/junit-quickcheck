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

package com.pholser.junit.quickcheck.internal.generator;

import java.util.HashSet;
import java.util.Set;

import com.pholser.junit.quickcheck.generator.Generator;

import static com.google.common.collect.Sets.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

public final class Generators {
    private Generators() {
        throw new UnsupportedOperationException();
    }

    @SafeVarargs
    public static void assertGenerators(Generator<?> result, Class<? extends Generator>... expectedTypes) {
        assumeThat(result, instanceOf(CompositeGenerator.class));

        CompositeGenerator composite = (CompositeGenerator) result;
        Set<Class<?>> compositeTypes = new HashSet<>();
        for (int i = 0; i < composite.numberOfComposedGenerators(); ++i)
            compositeTypes.add(composite.composed(i).getClass());

        assertEquals(newHashSet(expectedTypes), compositeTypes);
    }

    public static Generator<?> componentOf(Generator<?> generator, int index) {
        return ((CompositeGenerator) generator).composed(index);
    }
}

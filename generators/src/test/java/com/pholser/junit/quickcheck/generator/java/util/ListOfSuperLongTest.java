/*
 The MIT License

 Copyright (c) 2010-2013 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.generator.java.util;

import java.lang.reflect.Type;
import java.util.List;

import static java.util.Arrays.*;
import static java.util.Collections.*;

import com.pholser.junit.quickcheck.Generating;
import com.pholser.junit.quickcheck.generator.BasicGeneratorTheoryParameterTest;
import com.pholser.junit.quickcheck.internal.Reflection;
import org.javaruntype.type.Types;

import static com.google.common.collect.Lists.*;
import static com.pholser.junit.quickcheck.generator.RangeAttributes.*;
import static com.pholser.junit.quickcheck.reflect.ParameterizedTypeImpl.*;
import static com.pholser.junit.quickcheck.reflect.WildcardTypeImpl.*;
import static org.mockito.Mockito.*;

public class ListOfSuperLongTest extends BasicGeneratorTheoryParameterTest {
    @Override protected void primeSourceOfRandomness() {
        when(Generating.ints(randomForParameterGenerator, 0, 100)).thenReturn(2).thenReturn(1);
        when(randomForParameterGenerator.nextLong(minLong(), maxLong()))
            .thenReturn(3L).thenReturn(4L).thenReturn(5L);
        org.javaruntype.type.Type<?> longType = Types.forJavaLangReflectType(Long.class);
        List<org.javaruntype.type.Type<?>> supertypes = newArrayList(Reflection.supertypes(longType));
        when(Generating.ints(randomForGeneratorRepo, eq(0), anyInt()))
            .thenReturn(supertypes.indexOf(longType))
            .thenReturn(0)
            .thenReturn(supertypes.indexOf(longType))
            .thenReturn(0)
            .thenReturn(supertypes.indexOf(longType))
            .thenReturn(0);
    }

    @Override protected Type parameterType() {
        return parameterized(List.class).on(superOf(Long.class));
    }

    @Override protected int sampleSize() {
        return 3;
    }

    @SuppressWarnings("unchecked")
    @Override protected List<?> randomValues() {
        return asList(emptyList(), asList(3L), asList(4L, 5L));
    }

    @Override public void verifyInteractionWithRandomness() {
        verify(randomForParameterGenerator, times(3)).nextLong(minLong(), maxLong());
    }
}

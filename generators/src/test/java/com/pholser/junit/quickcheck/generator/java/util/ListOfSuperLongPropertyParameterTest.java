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

package com.pholser.junit.quickcheck.generator.java.util;

import java.util.List;

import com.pholser.junit.quickcheck.Generating;
import com.pholser.junit.quickcheck.generator.BasicGeneratorPropertyParameterTest;
import com.pholser.junit.quickcheck.internal.Reflection;
import org.javaruntype.type.Types;

import static com.google.common.collect.Lists.*;
import static com.pholser.junit.quickcheck.Generating.*;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.eq;

public class ListOfSuperLongPropertyParameterTest
    extends BasicGeneratorPropertyParameterTest {

    public static final List<? super Long> TYPE_BEARER = null;

    @Override protected void primeSourceOfRandomness() {
        when(Generating.ints(randomForParameterGenerator, 0, 100))
            .thenReturn(2).thenReturn(1);
        when(Generating.longs(randomForParameterGenerator))
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
        when(distro.sampleWithMean(1, randomForParameterGenerator)).thenReturn(0);
        when(distro.sampleWithMean(2, randomForParameterGenerator)).thenReturn(1);
        when(distro.sampleWithMean(3, randomForParameterGenerator)).thenReturn(2);
    }

    @Override protected int trials() {
        return 3;
    }

    @SuppressWarnings("unchecked")
    @Override protected List<?> randomValues() {
        return asList(emptyList(), singletonList(3L), asList(4L, 5L));
    }

    @Override public void verifyInteractionWithRandomness() {
        verifyLongs(randomForParameterGenerator, times(3));
    }
}

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
import java.util.Set;

import com.pholser.junit.quickcheck.Generating;
import com.pholser.junit.quickcheck.generator.BasicGeneratorPropertyParameterTest;
import com.pholser.junit.quickcheck.internal.Reflection;
import org.javaruntype.type.Types;

import static com.google.common.collect.Lists.*;
import static com.google.common.collect.Sets.*;
import static com.pholser.junit.quickcheck.Generating.*;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

public class SetOfSuperFloatPropertyParameterTest
    extends BasicGeneratorPropertyParameterTest {

    public static final Set<? super Float> TYPE_BEARER = null;

    @Override protected void primeSourceOfRandomness() {
        when(Generating.floats(randomForParameterGenerator))
            .thenReturn(0.2F).thenReturn(0.3F).thenReturn(0.4F);
        org.javaruntype.type.Type<?> floatType = Types.forJavaLangReflectType(Float.class);
        List<org.javaruntype.type.Type<?>> supertypes = newArrayList(Reflection.supertypes(floatType));
        when(Generating.ints(randomForGeneratorRepo, eq(0), anyInt()))
            .thenReturn(supertypes.indexOf(floatType))
            .thenReturn(0)
            .thenReturn(supertypes.indexOf(floatType))
            .thenReturn(0)
            .thenReturn(supertypes.indexOf(floatType))
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
        return asList(newHashSet(), newHashSet(0.2F), newHashSet(0.3F, 0.4F));
    }

    @Override public void verifyInteractionWithRandomness() {
        verifyFloats(randomForParameterGenerator, times(3));
    }
}

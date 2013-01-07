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

package com.pholser.junit.quickcheck.generator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.*;

import com.google.common.collect.Iterables;
import com.pholser.junit.quickcheck.internal.generator.GeneratingUniformRandomValuesForTheoryParameterTest;
import com.pholser.junit.quickcheck.reflect.GenericArrayTypeImpl;
import com.pholser.junit.quickcheck.reflect.ParameterizedTypeImpl;
import com.pholser.junit.quickcheck.reflect.WildcardTypeImpl;

import static org.mockito.Mockito.*;

public class ArrayOfListOfHuhTest extends GeneratingUniformRandomValuesForTheoryParameterTest {
    @Override protected void primeSourceOfRandomness() {
        int dateIndex = 13;
        when(randomForParameterGenerator.nextLong(Integer.MIN_VALUE, Long.MAX_VALUE))
            .thenReturn(1L).thenReturn(7L).thenReturn(63L).thenReturn(127L)
            .thenReturn(255L);
        when(randomForGeneratorRepo.nextInt(eq(0), anyInt())).thenReturn(0);
        when(randomForGeneratorRepo.nextInt(0, Iterables.size(source) - 4)).thenReturn(dateIndex);
    }

    @Override protected Type parameterType() {
        return new GenericArrayTypeImpl(
            new ParameterizedTypeImpl(List.class, new WildcardTypeImpl(new Type[] { Object.class }, new Type[0])));
    }

    @Override protected int sampleSize() {
        return 3;
    }

    @Override protected List<?> randomValues() {
        List<List<?>[]> values = new ArrayList<List<?>[]>();
        values.add(new List<?>[0]);
        values.add(new List<?>[] { asList(new Date(1L)) });
        values.add(new List<?>[] { asList(new Date(7L), new Date(63L)), asList(new Date(127L), new Date(255L)) });
        return values;
    }

    @Override public void verifyInteractionWithRandomness() {
        verify(randomForParameterGenerator, times(5)).nextLong(Integer.MIN_VALUE, Long.MAX_VALUE);
        verifyNoMoreInteractions(randomForParameterGenerator);
        verify(randomForGeneratorRepo, times(3)).nextInt(0, Iterables.size(source) - 4);
        verifyNoMoreInteractions(randomForGeneratorRepo);
    }
}

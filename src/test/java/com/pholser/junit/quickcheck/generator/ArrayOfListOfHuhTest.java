/*
 The MIT License

 Copyright (c) 2010-2012 Paul R. Holser, Jr.

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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.pholser.junit.quickcheck.internal.generator.GeneratingUniformRandomValuesForTheoryParameterTest;
import com.pholser.junit.quickcheck.reflect.GenericArrayTypeImpl;
import com.pholser.junit.quickcheck.reflect.ParameterizedTypeImpl;
import com.pholser.junit.quickcheck.reflect.WildcardTypeImpl;

import static com.pholser.junit.quickcheck.Strings.*;
import static java.util.Arrays.*;
import static org.mockito.Mockito.*;

public class ArrayOfListOfHuhTest extends GeneratingUniformRandomValuesForTheoryParameterTest {
    @Override
    protected void primeSourceOfRandomness() {
        int bigDecimalIndex = Iterables.indexOf(source, Predicates.instanceOf(BigDecimalGenerator.class));
        when(randomForParameterGenerator.nextBytes(0)).thenReturn(new byte[0]);
        when(randomForParameterGenerator.nextBytes(1)).thenReturn(bytesOf("a"));
        when(randomForParameterGenerator.nextBytes(2)).thenReturn(bytesOf("bc"))
            .thenReturn(bytesOf("de"))
            .thenReturn(bytesOf("fg"))
            .thenReturn(bytesOf("hi"));
        when(randomForGeneratorRepo.nextInt(eq(0), anyInt())).thenReturn(0);
        when(randomForGeneratorRepo.nextInt(0, Iterables.size(source) - 4)).thenReturn(bigDecimalIndex);
    }

    @Override
    protected Type parameterType() {
        return new GenericArrayTypeImpl(
            new ParameterizedTypeImpl(List.class, new WildcardTypeImpl(new Type[] { Object.class }, new Type[0])));
    }

    @Override
    protected int sampleSize() {
        return 3;
    }

    @Override
    protected List<?> randomValues() {
        List<List<?>[]> values = new ArrayList<List<?>[]>();
        values.add(new List<?>[0]);
        values.add(new List<?>[] { asList(new BigDecimal(new BigInteger(bytesOf("a")), 1)) });
        values.add(new List<?>[] {
            asList(new BigDecimal(new BigInteger(bytesOf("bc")), 2), new BigDecimal(new BigInteger(bytesOf("de")), 2)),
            asList(new BigDecimal(new BigInteger(bytesOf("fg")), 2), new BigDecimal(new BigInteger(bytesOf("hi")), 2)),
        });
        return values;
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(randomForParameterGenerator).nextBytes(1);
        verify(randomForParameterGenerator, times(4)).nextBytes(2);
        verifyNoMoreInteractions(randomForParameterGenerator);
        verify(randomForGeneratorRepo, times(3)).nextInt(0, Iterables.size(source) - 4);
        verifyNoMoreInteractions(randomForGeneratorRepo);
    }
}

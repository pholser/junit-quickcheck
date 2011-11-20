/*
 The MIT License

 Copyright (c) 2010-2011 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.internal.generate;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.pholser.junit.quickcheck.internal.extractors.IntegerExtractor;
import com.pholser.junit.quickcheck.reflect.ParameterizedTypeImpl;
import com.pholser.junit.quickcheck.reflect.WildcardTypeImpl;

import static com.google.common.collect.Sets.*;
import static java.util.Arrays.*;
import static org.mockito.Mockito.*;

public class GeneratingUniformRandomValuesForSetOfHuhTheoryParametersTest
    extends GeneratingUniformRandomValuesForTheoryParameterTest {

    @Override
    protected void primeSourceOfRandomness() {
        int integerIndex = Iterables.indexOf(source, Predicates.instanceOf(IntegerExtractor.class));
        when(random.nextInt(0, 100)).thenReturn(2);
        when(random.nextInt(0, Iterables.size(source) - 4))
            .thenReturn(integerIndex).thenReturn(integerIndex);
        when(random.nextInt()).thenReturn(4).thenReturn(4).thenReturn(5);
    }

    @Override
    protected Type parameterType() {
        return new ParameterizedTypeImpl(Set.class, new WildcardTypeImpl(new Type[0], new Type[0]));
    }

    @Override
    protected int sampleSize() {
        return 1;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List<?> randomValues() {
        return asList(newHashSet(4, 5));
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random).nextInt(0, 100);
        verify(random, times(3)).nextInt(0, Iterables.size(source) - 4);
        verify(random, times(3)).nextInt();
    }
}

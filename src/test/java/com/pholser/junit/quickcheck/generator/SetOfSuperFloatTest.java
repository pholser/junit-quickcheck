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
import java.util.List;
import java.util.Set;

import com.pholser.junit.quickcheck.internal.generator.GeneratingUniformRandomValuesForTheoryParameterTest;
import com.pholser.junit.quickcheck.reflect.ParameterizedTypeImpl;
import com.pholser.junit.quickcheck.reflect.WildcardTypeImpl;

import static com.google.common.collect.Sets.*;
import static java.util.Arrays.*;
import static org.mockito.Mockito.*;

public class SetOfSuperFloatTest extends GeneratingUniformRandomValuesForTheoryParameterTest {
    @Override
    protected void primeSourceOfRandomness() {
        when(randomForParameterGenerator.nextFloat()).thenReturn(2.2F).thenReturn(2.3F).thenReturn(2.4F);
    }

    @Override
    protected Type parameterType() {
        return new ParameterizedTypeImpl(Set.class, new WildcardTypeImpl(new Type[0], new Type[] { Float.class }));
    }

    @Override
    protected int sampleSize() {
        return 3;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List<?> randomValues() {
        return asList(newHashSet(), newHashSet(2.2F), newHashSet(2.3F, 2.4F));
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(randomForParameterGenerator, times(3)).nextFloat();
    }
}

/*
 The MIT License

 Copyright (c) 2010-2020 Paul R. Holser, Jr.

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

import java.util.List;

import com.pholser.junit.quickcheck.internal.generator.CorePropertyParameterTest;

import static java.util.Arrays.*;
import static org.mockito.Mockito.*;

public class PrimitiveIntegerArrayPropertyParameterTest extends CorePropertyParameterTest {
    public static final int[] TYPE_BEARER = null;

    @Override protected void primeSourceOfRandomness() {
        when(randomForParameterGenerator.nextInt())
            .thenReturn(-1).thenReturn(-2).thenReturn(2);
        when(distro.sampleWithMean(1, randomForParameterGenerator)).thenReturn(0);
        when(distro.sampleWithMean(2, randomForParameterGenerator)).thenReturn(1);
        when(distro.sampleWithMean(3, randomForParameterGenerator)).thenReturn(2);
    }

    @Override protected int trials() {
        return 3;
    }

    @Override protected List<?> randomValues() {
        return asList(new int[0], new int[] { -1 }, new int[] { -2, 2 });
    }

    @Override public void verifyInteractionWithRandomness() {
        verify(randomForParameterGenerator, times(3)).nextInt();
    }
}

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
import java.util.List;

import static java.lang.Byte.*;
import static java.util.Arrays.*;

import com.pholser.junit.quickcheck.internal.generator.GeneratingUniformRandomValuesForTheoryParameterTest;

import static org.mockito.Mockito.*;

public class WrapperByteTest extends GeneratingUniformRandomValuesForTheoryParameterTest {
    @Override protected void primeSourceOfRandomness() {
        when(randomForParameterGenerator.nextByte(MIN_VALUE, MAX_VALUE))
            .thenReturn((byte) -95).thenReturn((byte) -94).thenReturn((byte) -93).thenReturn((byte) -92);
    }

    @Override protected Type parameterType() {
        return Byte.class;
    }

    @Override protected int sampleSize() {
        return 4;
    }

    @Override protected List<?> randomValues() {
        byte b = (byte) 0xA1;
        return asList(b++, b++, b++, b);
    }

    @Override public void verifyInteractionWithRandomness() {
        verify(randomForParameterGenerator, times(4)).nextByte(MIN_VALUE, MAX_VALUE);
    }
}

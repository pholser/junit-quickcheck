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
import java.math.BigInteger;
import java.util.List;

import static com.pholser.junit.quickcheck.Strings.*;
import static java.util.Arrays.*;
import static org.mockito.Mockito.*;

public class BigIntegerTest extends GeneratingUniformRandomValuesForTheoryParameterTest {
    @Override
    protected void primeSourceOfRandomness() {
        when(random.nextBytes(0)).thenReturn(new byte[0]);
        when(random.nextBytes(1)).thenReturn(bytesOf("a"));
        when(random.nextBytes(2)).thenReturn(bytesOf("bc"));
        when(random.nextBytes(3)).thenReturn(bytesOf("def"));
    }

    @Override
    protected Type parameterType() {
        return BigInteger.class;
    }

    @Override
    protected int sampleSize() {
        return 4;
    }

    @Override
    protected List<?> randomValues() {
        return asList(BigInteger.ZERO, new BigInteger(bytesOf("a")), new BigInteger(bytesOf("bc")),
            new BigInteger(bytesOf("def")));
    }

    @Override
    public void verifyInteractionWithRandomness() {
        verify(random).nextBytes(0);
        verify(random).nextBytes(1);
        verify(random).nextBytes(2);
        verify(random).nextBytes(3);
    }
}

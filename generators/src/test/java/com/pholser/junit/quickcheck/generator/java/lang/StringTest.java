/*
 The MIT License

 Copyright (c) 2010-2014 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.generator.java.lang;

import com.pholser.junit.quickcheck.Generating;
import com.pholser.junit.quickcheck.generator.BasicGeneratorTheoryParameterTest;
import com.pholser.junit.quickcheck.generator.Generator;

import java.lang.reflect.Type;
import java.util.List;

import static com.pholser.junit.quickcheck.Generating.verifyCharsForString;
import static java.util.Arrays.*;
import static org.mockito.Mockito.*;

public class StringTest extends BasicGeneratorTheoryParameterTest {
    @Override protected void primeSourceOfRandomness() {
        when(Generating.charsForString(randomForParameterGenerator))
            .thenReturn(0x61).thenReturn(0x62).thenReturn(0x63).thenReturn(0x64).thenReturn(0x65).thenReturn(0x66);
    }

    @Override protected Type parameterType() {
        return String.class;
    }

    @SuppressWarnings("unchecked")
    @Override protected Class<? extends Generator>[] explicitGenerators() {
        return (Class<? extends Generator>[]) new Class<?>[] { StringGenerator.class };
    }

    @Override protected int sampleSize() {
        return 4;
    }

    @Override protected List<?> randomValues() {
        return asList("", "a", "bc", "def");
    }

    @Override public void verifyInteractionWithRandomness() {
        verifyCharsForString(randomForParameterGenerator, times(6));
    }
}

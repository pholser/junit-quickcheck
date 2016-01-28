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

package com.pholser.junit.quickcheck.generator.java.nio.charset;

import com.pholser.junit.quickcheck.generator.BasicGeneratorPropertyParameterTest;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

public class CharsetPropertyParameterTest extends BasicGeneratorPropertyParameterTest {
    public static final Charset TYPE_BEARER = null;

    private static final List<String> CHARSET_NAMES =
        new ArrayList<>(Charset.availableCharsets().keySet());

    @Override protected void primeSourceOfRandomness() {
        when(randomForParameterGenerator.choose(Charset.availableCharsets().keySet()))
            .thenReturn(CHARSET_NAMES.get(2))
            .thenReturn(CHARSET_NAMES.get(0))
            .thenReturn(CHARSET_NAMES.get(1));
    }

    @Override protected int trials() {
        return 3;
    }

    @Override protected List<?> randomValues() {
        return asList(
            Charset.forName(CHARSET_NAMES.get(2)),
            Charset.forName(CHARSET_NAMES.get(0)),
            Charset.forName(CHARSET_NAMES.get(1)));
    }

    @Override public void verifyInteractionWithRandomness() {
        verify(randomForParameterGenerator, times(3))
            .choose(Charset.availableCharsets().keySet());
    }
}

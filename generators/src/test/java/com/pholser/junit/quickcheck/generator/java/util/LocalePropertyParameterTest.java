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
import java.util.Locale;

import com.pholser.junit.quickcheck.generator.BasicGeneratorPropertyParameterTest;

import static java.util.Arrays.*;
import static org.mockito.Mockito.*;

public class LocalePropertyParameterTest extends BasicGeneratorPropertyParameterTest {
    public static final Locale TYPE_BEARER = null;

    private static final Locale[] LOCALES = Locale.getAvailableLocales();

    @Override protected void primeSourceOfRandomness() {
        when(randomForParameterGenerator.choose(LOCALES))
            .thenReturn(LOCALES[1]).thenReturn(LOCALES[0]).thenReturn(LOCALES[2]);
    }

    @Override protected int trials() {
        return 3;
    }

    @Override protected List<?> randomValues() {
        return asList(LOCALES[1], LOCALES[0], LOCALES[2]);
    }

    @Override public void verifyInteractionWithRandomness() {
        verify(randomForParameterGenerator, times(3)).choose(LOCALES);
    }
}

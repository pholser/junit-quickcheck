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

package com.pholser.junit.quickcheck.generator.java.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pholser.junit.quickcheck.generator.BasicGeneratorTheoryParameterTest;
import com.pholser.junit.quickcheck.generator.InRange;

import static java.util.Arrays.*;
import static org.mockito.Mockito.*;

public class RangedDateTest extends BasicGeneratorTheoryParameterTest {
    private final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    private Date first;
    private Date second;
    private Date third;
    private Date min;
    private Date max;

    @Override protected void primeSourceOfRandomness() throws Exception {
        first = formatter.parse("12/25/945");
        second = formatter.parse("7/4/1776");
        third = formatter.parse("8/26/2008");
        min = formatter.parse("1/1/500");
        max = formatter.parse("12/31/2020");
        when(randomForParameterGenerator.nextLong(min.getTime(), max.getTime()))
            .thenReturn(first.getTime()).thenReturn(second.getTime()).thenReturn(third.getTime());
    }

    @Override protected Type parameterType() {
        return Date.class;
    }

    @Override protected int sampleSize() {
        return 3;
    }

    @Override protected List<?> randomValues() {
        return asList(new Date(first.getTime()), new Date(second.getTime()), new Date(third.getTime()));
    }

    @Override protected Map<Class<? extends Annotation>, Annotation> configurations() {
        InRange range = mock(InRange.class);
        when(range.min()).thenReturn("1/1/500");
        when(range.max()).thenReturn("12/31/2020");
        when(range.format()).thenReturn("MM/dd/yyyy");
        return Collections.<Class<? extends Annotation>, Annotation> singletonMap(InRange.class, range);
    }

    @Override public void verifyInteractionWithRandomness() {
        verify(randomForParameterGenerator, times(3)).nextLong(min.getTime(), max.getTime());
    }
}

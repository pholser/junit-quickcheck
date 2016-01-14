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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.pholser.junit.quickcheck.Generating;
import com.pholser.junit.quickcheck.generator.BasicGeneratorPropertyParameterTest;
import com.pholser.junit.quickcheck.generator.InRange;

import static com.pholser.junit.quickcheck.Generating.*;
import static java.util.Arrays.*;
import static org.mockito.Mockito.*;

public class RangedDatePropertyParameterTest
    extends BasicGeneratorPropertyParameterTest {

    @InRange(min = "1/1/500", max = "12/31/2020", format = "MM/dd/yyyy")
    public static final Date TYPE_BEARER = null;

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
        when(Generating.longs(randomForParameterGenerator, min.getTime(), max.getTime()))
            .thenReturn(first.getTime())
            .thenReturn(second.getTime())
            .thenReturn(third.getTime());
    }

    @Override protected int trials() {
        return 3;
    }

    @Override protected List<?> randomValues() {
        return asList(new Date(first.getTime()), new Date(second.getTime()), new Date(third.getTime()));
    }

    @Override public void verifyInteractionWithRandomness() {
        verifyLongs(randomForParameterGenerator, times(3), min.getTime(), max.getTime());
    }
}

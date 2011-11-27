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

package com.pholser.junit.quickcheck.internal;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.RandomValueExtractor;

public class ParameterContext {
    private final Type parameterType;
    private final List<RandomValueExtractor<?>> extractors = new ArrayList<RandomValueExtractor<?>>();
    private int sampleSize;

    public ParameterContext(Type parameterType) {
        this.parameterType = parameterType;
    }

    public ParameterContext addQuantifier(ForAll quantifier) {
        this.sampleSize = quantifier.sampleSize();
        return this;
    }

    public ParameterContext addExtractors(Class<? extends RandomValueExtractor>... extractors) {
        for (Class<? extends RandomValueExtractor> each : extractors)
            this.extractors.add(Reflection.instantiate(each));

        return this;
    }

    public Type parameterType() {
        return parameterType;
    }

    public int sampleSize() {
        return sampleSize;
    }
}

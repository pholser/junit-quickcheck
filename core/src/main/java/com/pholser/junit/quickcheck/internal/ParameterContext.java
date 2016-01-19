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

package com.pholser.junit.quickcheck.internal;

import java.lang.reflect.AnnotatedElement;

import com.pholser.junit.quickcheck.ForAll;

import static com.pholser.junit.quickcheck.internal.Reflection.*;

@Deprecated
public class ParameterContext {
    private final ParameterTypeContext typeContext;

    private int configuredSampleSize;
    private SampleSizer sampleSizer;
    private int discardRatio;
    private String constraint;
    private long seed = (long) defaultValueOf(ForAll.class, "seed");

    public ParameterContext(ParameterTypeContext typeContext) {
        this.typeContext = typeContext;
    }

    public ParameterContext annotate(AnnotatedElement element) {
        ForAll quantifier = element.getAnnotation(ForAll.class);
        addQuantifier(quantifier);
        addConstraint(quantifier);
        typeContext.annotate(element);

        return this;
    }

    public ParameterContext addQuantifier(ForAll quantifier) {
        if (quantifier != null) {
            this.configuredSampleSize = quantifier.sampleSize();
            this.discardRatio = quantifier.discardRatio();
            this.seed = quantifier.seed();
        }

        return this;
    }

    public ParameterContext addConstraint(ForAll quantifier) {
        if (quantifier != null && !defaultValueOf(ForAll.class, "suchThat").equals(quantifier.suchThat()))
            constraint = quantifier.suchThat();

        return this;
    }

    public ParameterTypeContext typeContext() {
        return typeContext;
    }

    public int sampleSize() {
        if (sampleSizer == null)
            sampleSizer = new SampleSizer(configuredSampleSize, typeContext);

        return sampleSizer.sampleSize();
    }

    public int discardRatio() {
        return discardRatio;
    }

    public String constraint() {
        return constraint;
    }

    public boolean fixedSeed() {
        return seed != (long) defaultValueOf(ForAll.class, "seed");
    }

    public long seed() {
        return seed;
    }
}

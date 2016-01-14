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

import com.pholser.junit.quickcheck.When;

import static com.pholser.junit.quickcheck.internal.Reflection.*;

public class PropertyParameterContext {
    private final ParameterTypeContext typeContext;
    private final int sampleSize;

    private int discardRatio;
    private String constraint;
    private long seed = (long) defaultValueOf(When.class, "seed");

    public PropertyParameterContext(ParameterTypeContext typeContext, int sampleSize) {
        this.typeContext = typeContext;
        this.sampleSize = sampleSize;
    }

    public PropertyParameterContext annotate(AnnotatedElement element) {
        When quantifier = element.getAnnotation(When.class);
        addQuantifier(quantifier);
        addConstraint(quantifier);
        typeContext.annotate(element);

        return this;
    }

    public PropertyParameterContext addQuantifier(When quantifier) {
        if (quantifier != null) {
            this.discardRatio = ensureNonNegativeDiscardRatio(quantifier);
            this.seed = quantifier.seed();
        }

        return this;
    }

    public PropertyParameterContext addConstraint(When quantifier) {
        if (quantifier != null && !defaultValueOf(When.class, "satisfies").equals(quantifier.satisfies()))
            constraint = quantifier.satisfies();

        return this;
    }

    public ParameterTypeContext typeContext() {
        return typeContext;
    }

    public int sampleSize() {
        return sampleSize;
    }

    public int discardRatio() {
        return discardRatio;
    }

    public String constraint() {
        return constraint;
    }

    public boolean fixedSeed() {
        return seed != (long) defaultValueOf(When.class, "seed");
    }

    public long seed() {
        return seed;
    }

    private int ensureNonNegativeDiscardRatio(When quantifier) {
        if (quantifier.discardRatio() < 0) {
            throw new IllegalArgumentException(
                "Negative discard ratio for parameter " + typeContext.name()
                    + " of type " + typeContext.type().getTypeName());
        }

        return quantifier.discardRatio();
    }
}

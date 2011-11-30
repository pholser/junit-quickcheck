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

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.RandomValueExtractor;
import com.pholser.junit.quickcheck.internal.extractors.ExtractorRepository;
import org.javaruntype.type.Types;

public class ParameterContext {
    private static final String EXPLICIT_EXTRACTOR_TYPE_MISMATCH_MESSAGE =
        "The extractor %s named in @%s on parameter of type %s does not produce a type-compatible object";

    private final Type parameterType;
    private final ExtractorRepository repo = new ExtractorRepository();
    private int sampleSize;

    public ParameterContext(Type parameterType) {
        this.parameterType = parameterType;
    }

    public ParameterContext addQuantifier(ForAll quantifier) {
        this.sampleSize = quantifier.sampleSize();
        return this;
    }

    public ParameterContext addExtractors(From extractors) {
        for (Class<? extends RandomValueExtractor> each : extractors.value()) {
            RandomValueExtractor<?> extractor = Reflection.instantiate(each);
            ensureCorrectType(extractor);
            repo.add(extractor);
        }

        return this;
    }

    private void ensureCorrectType(RandomValueExtractor<?> extractor) {
        org.javaruntype.type.Type<?> parmType = Types.forJavaLangReflectType(parameterType);
        for (Class<?> each : extractor.types()) {
            org.javaruntype.type.Type<?> extractorType = Types.forJavaLangReflectType(each);
            if (!parmType.isAssignableFrom(extractorType)) {
                throw new IllegalArgumentException(String.format(EXPLICIT_EXTRACTOR_TYPE_MISMATCH_MESSAGE, each,
                    From.class.getName(), parameterType));
            }
        }
    }

    public Type parameterType() {
        return parameterType;
    }

    public int sampleSize() {
        return sampleSize;
    }

    public RandomValueExtractor<?> extractor() {
        return repo.isEmpty() ? null : repo.extractorFor(parameterType);
    }
}

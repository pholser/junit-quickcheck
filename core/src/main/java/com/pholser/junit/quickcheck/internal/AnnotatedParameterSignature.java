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

package com.pholser.junit.quickcheck.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.junit.contrib.theories.ParameterSignature;

class AnnotatedParameterSignature implements AnnotatedElement {
    private final ParameterSignature signature;

    AnnotatedParameterSignature(ParameterSignature signature) {
        this.signature = signature;
    }

    @Override public boolean isAnnotationPresent(Class<? extends Annotation> c) {
        return signature.hasAnnotation(c);
    }

    @Override public <T extends Annotation> T getAnnotation(Class<T> c) {
        return signature.getAnnotation(c);
    }

    @Override public Annotation[] getAnnotations() {
        return signature.getAnnotations().toArray(new Annotation[signature.getAnnotations().size()]);
    }

    @Override public Annotation[] getDeclaredAnnotations() {
        return getAnnotations();
    }
}

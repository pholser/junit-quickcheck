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
import java.lang.reflect.TypeVariable;
import java.util.List;

import com.pholser.junit.quickcheck.reflect.GenericArrayTypeImpl;
import com.pholser.junit.quickcheck.reflect.WildcardTypeImpl;

import com.pholser.junit.quickcheck.reflect.ParameterizedTypeImpl;

import org.javaruntype.exceptions.TypeValidationException;

import com.pholser.junit.quickcheck.internal.extractors.BasicExtractorSource;
import com.pholser.junit.quickcheck.internal.extractors.ExtractorRepository;
import com.pholser.junit.quickcheck.reflect.TypeVariableImpl;
import org.junit.Before;
import org.junit.Test;

public class FindingExtractorInvolvingTypeVariableTest {
    private ExtractorRepository repo;
    private TypeVariable<Class<?>> typeVariable;

    @Before
    public void setUp() {
        repo = new ExtractorRepository().add(new BasicExtractorSource());
        typeVariable = new TypeVariableImpl<Class<?>>("E", List.class, new Type[] { Object.class });
    }

    @Test(expected = TypeValidationException.class)
    public void withBareTypeVariable() {
        repo.extractorFor(typeVariable);
    }

    @Test(expected = TypeValidationException.class)
    public void withTypeVariableInParameterizedType() {
        repo.extractorFor(new ParameterizedTypeImpl(List.class, typeVariable));
    }

    @Test(expected = TypeValidationException.class)
    public void withTypeVariableInBoundsOfParameterizedType() {
        repo.extractorFor(new ParameterizedTypeImpl(List.class,
            new WildcardTypeImpl(new Type[] { typeVariable }, new Type[0])));
    }

    @Test(expected = TypeValidationException.class)
    public void withTypeVariableInGenericArrayType() {
        repo.extractorFor(new GenericArrayTypeImpl(typeVariable));
    }
}

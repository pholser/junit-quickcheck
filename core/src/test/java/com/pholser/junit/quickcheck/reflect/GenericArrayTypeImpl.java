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

package com.pholser.junit.quickcheck.reflect;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

import com.google.common.base.Objects;

public class GenericArrayTypeImpl implements GenericArrayType {
    private final Type componentType;

    public GenericArrayTypeImpl(Type componentType) {
        this.componentType = componentType;
    }

    @Override public Type getGenericComponentType() {
        return componentType;
    }

    @Override public boolean equals(Object that) {
        if (this == that)
            return true;

        if (!(that instanceof GenericArrayType))
            return false;

        GenericArrayType other = (GenericArrayType) that;
        return Objects.equal(getGenericComponentType(), other.getGenericComponentType());
    }

    @Override public int hashCode() {
        return Objects.hashCode(getGenericComponentType());
    }

    @Override public String toString() {
        return getGenericComponentType().toString() + "[]";
    }
}

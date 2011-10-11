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

package com.pholser.junit.quickcheck.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import static java.lang.System.*;

public final class ParameterizedTypeImpl implements ParameterizedType {
    private final Type raw;
    private final Type[] actualTypeArguments;

    public ParameterizedTypeImpl(Type raw, Type... actualTypeArguments) {
        this.raw = raw;
        this.actualTypeArguments = new Type[actualTypeArguments.length];
        arraycopy(actualTypeArguments, 0, this.actualTypeArguments, 0, actualTypeArguments.length);
    }

    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArguments.clone();
    }

    @Override
    public Type getRawType() {
        return raw;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;
        if (!(that instanceof ParameterizedType))
            return false;

        ParameterizedType other = (ParameterizedType) that;
        Type otherOwner = other.getOwnerType();
        Type otherRaw = other.getRawType();

        return (getOwnerType() == null ? otherOwner == null : getOwnerType().equals(otherOwner))
            && (raw == null ? otherRaw == null : raw.equals(otherRaw))
            && Arrays.equals(actualTypeArguments, other.getActualTypeArguments());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(actualTypeArguments)
            ^ (getOwnerType() == null ? 0 : getOwnerType().hashCode())
            ^ (raw == null ? 0 : raw.hashCode());
    }
}

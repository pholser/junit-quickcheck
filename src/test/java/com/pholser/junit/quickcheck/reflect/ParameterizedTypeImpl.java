/*
 The MIT License

 Copyright (c) 2010-2012 Paul R. Holser, Jr.

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

import com.google.common.base.Joiner;
import com.google.common.base.Objects;

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
        if (this == that) {
            return true;
        }
        if (!(that instanceof ParameterizedType)) {
            return false;
        }

        ParameterizedType other = (ParameterizedType) that;
        return Objects.equal(getOwnerType(), other.getOwnerType())
            && Objects.equal(getRawType(), other.getRawType())
            && Arrays.equals(getActualTypeArguments(), other.getActualTypeArguments());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getOwnerType(), getRawType(), Arrays.hashCode(getActualTypeArguments()));
    }

    @Override
    public String toString() {
        return raw.toString() + '<' + Joiner.on(", ").join(actualTypeArguments) + '>';
    }
}

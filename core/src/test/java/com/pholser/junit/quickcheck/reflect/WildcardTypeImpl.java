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

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;

import com.google.common.base.Joiner;

public class WildcardTypeImpl implements WildcardType {
    private final Type[] upperBounds;
    private final Type[] lowerBounds;

    private WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
        this.upperBounds = upperBounds.clone();
        this.lowerBounds = lowerBounds.clone();
    }

    public static WildcardType huh() {
        return extendsOf();
    }

    public static WildcardType extendsOf(Type... extensions) {
        return new WildcardTypeImpl(extensions, new Type[0]);
    }

    public static WildcardType superOf(Type... extensions) {
        return new WildcardTypeImpl(new Type[0], extensions);
    }

    @Override public Type[] getUpperBounds() {
        return upperBounds.clone();
    }

    @Override public Type[] getLowerBounds() {
        return lowerBounds.clone();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (!(that instanceof WildcardType)) {
            return false;
        }

        WildcardType other = (WildcardType) that;
        return Arrays.equals(getUpperBounds(), other.getUpperBounds())
            && Arrays.equals(getLowerBounds(), other.getLowerBounds());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getUpperBounds()) ^ Arrays.hashCode(getLowerBounds());
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder("?");

        String upperBoundDisplay = Joiner.on(" & ").join(getUpperBounds());
        if (!upperBoundDisplay.isEmpty())
            buffer.append(" extends ").append(upperBoundDisplay);

        String lowerBoundDisplay = Joiner.on(" & ").join(getLowerBounds());
        if (!lowerBoundDisplay.isEmpty())
            buffer.append(" super ").append(lowerBoundDisplay);

        return buffer.toString();
    }
}

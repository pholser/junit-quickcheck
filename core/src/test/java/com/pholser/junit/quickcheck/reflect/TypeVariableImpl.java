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

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;

public class TypeVariableImpl<D extends GenericDeclaration> implements TypeVariable<D> {
    private final String name;
    private final D declaration;
    private final Type[] bounds;

    public TypeVariableImpl(String name, D declaration, Type[] bounds) {
        this.name = name;
        this.declaration = declaration;
        this.bounds = bounds.clone();
    }

    @Override public Type[] getBounds() {
        return bounds.clone();
    }

    @Override public D getGenericDeclaration() {
        return declaration;
    }

    @Override public String getName() {
        return name;
    }

    @Override public boolean equals(Object that) {
        if (this == that)
            return true;

        if (!(that instanceof TypeVariable<?>))
            return false;

        TypeVariable<?> other = (TypeVariable<?>) that;
        return Objects.equal(getName(), other.getName()) && Arrays.equals(getBounds(), other.getBounds())
            && Objects.equal(getGenericDeclaration(), other.getGenericDeclaration());
    }

    @Override public int hashCode() {
        return Objects.hashCode(getName(), getGenericDeclaration()) ^ Arrays.hashCode(getBounds());
    }

    @Override public String toString() {
        StringBuilder buffer = new StringBuilder(getName());

        String boundDisplay = Joiner.on(" & ").join(getBounds());
        if (!boundDisplay.isEmpty())
            buffer.append(" extends ").append(boundDisplay);

        return buffer.toString();
    }
}

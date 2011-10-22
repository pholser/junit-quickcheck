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

    @Override
    public Type[] getBounds() {
        return bounds.clone();
    }

    @Override
    public D getGenericDeclaration() {
        return declaration;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;

        if (!(that instanceof TypeVariable<?>))
            return false;

        TypeVariable<?> other = (TypeVariable<?>) that;
        return Objects.equal(getName(), other.getName()) && Arrays.equals(getBounds(), other.getBounds())
            && Objects.equal(getGenericDeclaration(), other.getGenericDeclaration());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName(), getGenericDeclaration()) ^ Arrays.hashCode(getBounds());
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder(getName());

        String boundDisplay = Joiner.on(" & ").join(getBounds());
        if (!boundDisplay.isEmpty())
            buffer.append(" extends ").append(boundDisplay);

        return buffer.toString();
    }
}

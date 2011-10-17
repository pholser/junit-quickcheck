package com.pholser.junit.quickcheck.reflect;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

import com.google.common.base.Objects;

public class GenericArrayTypeImpl implements GenericArrayType {
    private final Type componentType;

    public GenericArrayTypeImpl(Type componentType) {
        this.componentType = componentType;
    }

    @Override
    public Type getGenericComponentType() {
        return componentType;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;

        if (!(that instanceof GenericArrayType))
            return false;

        GenericArrayType other = (GenericArrayType) that;
        return Objects.equal(getGenericComponentType(), other.getGenericComponentType());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getGenericComponentType());
    }

    @Override
    public String toString() {
        return getGenericComponentType().toString() + "[]";
    }
}

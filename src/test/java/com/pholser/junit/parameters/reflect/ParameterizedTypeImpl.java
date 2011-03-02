package com.pholser.junit.parameters.reflect;

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

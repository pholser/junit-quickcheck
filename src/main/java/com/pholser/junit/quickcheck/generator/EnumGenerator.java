package com.pholser.junit.quickcheck.generator;

import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;

@SuppressWarnings("rawtypes")
public class EnumGenerator extends Generator<Enum> {
    private final Class<?> componentType;

    public EnumGenerator(Class<?> componentType) {
        super(Enum.class);

        this.componentType = componentType;
    }

    @Override
    public Enum<?> generate(SourceOfRandomness random, int size) {
    	Object[] enums = componentType.getEnumConstants();
    	// Prevent indexoutofbounds if we are called out of context
    	return (Enum<?>) enums[size % enums.length];
    }
}

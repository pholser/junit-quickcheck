package com.pholser.junit.parameters.internal;

import java.util.List;

import com.pholser.junit.parameters.ForAll;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParameterSupplier;
import org.junit.experimental.theories.PotentialAssignment;

public class RandomValueSupplier extends ParameterSupplier {
    private final TheoryParameterGenerator generator;

    public RandomValueSupplier() {
        this(new RandomTheoryParameterGenerator(new JDKSourceOfRandomness()));
    }

    protected RandomValueSupplier(TheoryParameterGenerator generator) {
        this.generator = generator;
    }

    @Override
    public List<PotentialAssignment> getValueSources(ParameterSignature signature) {
        return generator.generate(signature.getAnnotation(ForAll.class), signature.getType());
    }
}

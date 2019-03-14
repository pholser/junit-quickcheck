package com.pholser.junit.quickcheck.internal.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

public class ConventionGen extends Generator<Convention> {


    protected ConventionGen() {
        super(Convention.class);
    }

    @Override
    public Convention generate(SourceOfRandomness random, GenerationStatus status) {
        return new Convention();
    }
}

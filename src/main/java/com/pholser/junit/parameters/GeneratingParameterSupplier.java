package com.pholser.junit.parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pholser.junit.parameters.extractors.RandomValueExtractorSource;

import com.pholser.junit.parameters.extractors.ServiceLoaderExtractorSource;

import com.pholser.junit.parameters.extractors.RandomValueExtractor;
import com.pholser.junit.parameters.random.SecureJDKSourceOfRandomness;
import com.pholser.junit.parameters.random.SourceOfRandomness;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParameterSupplier;
import org.junit.experimental.theories.PotentialAssignment;

import static org.apache.commons.lang.ClassUtils.*;

public class GeneratingParameterSupplier extends ParameterSupplier {
    private final SourceOfRandomness random;
    private final Map<Class<?>, RandomValueExtractor<?>> extractors;

    public GeneratingParameterSupplier() {
        this(new SecureJDKSourceOfRandomness(), new ServiceLoaderExtractorSource());
    }

    public GeneratingParameterSupplier(SourceOfRandomness random, RandomValueExtractorSource extractorSource) {
        this.random = random;
        extractors = extractorSource.extractors();
    }

    @Override
    public List<PotentialAssignment> getValueSources(ParameterSignature sig) {
        RandomValueExtractor<?> extractor = extractor(sig);
        int sampleSize = sampleSizeFor(sig);

        List<PotentialAssignment> potentials = new ArrayList<PotentialAssignment>();
        for (int i = 0; i < sampleSize; ++i) {
            Object generated = extractor.randomValue(random);
            potentials.add(PotentialAssignment.forValue(String.valueOf(generated), generated));
        }
        return potentials;
    }

    private static int sampleSizeFor(ParameterSignature sig) {
        return sig.getAnnotation(ForAll.class).sampleSize();
    }

    private RandomValueExtractor<?> extractor(ParameterSignature sig) {
        Class<?> key = primitiveToWrapper(sig.getType());
        if (!extractors.containsKey(key))
            throw new IllegalStateException("Don't know how to generate values of " + sig.getType());
        return extractors.get(key);
    }
}

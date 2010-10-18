package com.pholser.junit.parameters;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParameterSupplier;
import org.junit.experimental.theories.PotentialAssignment;

public class GeneratingParameterSupplier extends ParameterSupplier {
    private static final Map<Class<?>, RandomValueExtractor> extractors =
        new HashMap<Class<?>, RandomValueExtractor>();

    static {
        extractors.put(int.class, new RandomValueExtractor() {
            @Override
            public Object randomValue(SourceOfRandomness random) {
                return random.nextInt();
            }
        });
        extractors.put(Integer.class, extractors.get(int.class));
        extractors.put(double.class, new RandomValueExtractor() {
            @Override
            public Object randomValue(SourceOfRandomness random) {
                return random.nextDouble();
            }
        });
        extractors.put(Double.class, extractors.get(double.class));
        extractors.put(String.class, new RandomValueExtractor() {
            @Override
            public Object randomValue(SourceOfRandomness random) {
                try {
                    return new String(random.nextBytes(16), "US-ASCII");
                } catch (UnsupportedEncodingException ex) {
                    throw new AssertionError(ex);
                }
            }
        });
    }

    private final SourceOfRandomness random;

    public GeneratingParameterSupplier() {
        this(new SecureJDKSourceOfRandomness());
    }

    public GeneratingParameterSupplier(SourceOfRandomness random) {
        this.random = random;
    }

    @Override
    public List<PotentialAssignment> getValueSources(ParameterSignature sig) {
        List<PotentialAssignment> potentials = new ArrayList<PotentialAssignment>();
        ForAll generationParms = sig.getAnnotation(ForAll.class);
        RandomValueExtractor extractor = extractor(sig);

        for (int i = 0; i < generationParms.sampleSize(); ++i) {
            Object generated = extractor.randomValue(random);
            potentials.add(PotentialAssignment.forValue(String.valueOf(generated), generated));
        }
        return potentials;
    }

    private RandomValueExtractor extractor(ParameterSignature sig) {
        ExtractedBy extractedBy = sig.getAnnotation(ExtractedBy.class);

        if (extractedBy != null)
            return instantiate(extractedBy.value());
        return extractors.get(sig.getType());
    }

    private RandomValueExtractor instantiate(Class<? extends RandomValueExtractor> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException ex) {
            throw new IllegalStateException(ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        }
    }
}

package com.pholser.junit.parameters.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pholser.junit.parameters.ForAll;
import org.junit.experimental.theories.PotentialAssignment;

public class RandomTheoryParameterGenerator implements TheoryParameterGenerator {
    private final SourceOfRandomness random;
    private final Map<Class<?>, RandomValueExtractor<?>> extractors =
        new HashMap<Class<?>, RandomValueExtractor<?>>();

    public RandomTheoryParameterGenerator(SourceOfRandomness random) {
        this.random = random;
        extractors.put(int.class, new IntegerExtractor());
        extractors.put(Integer.class, new IntegerExtractor());
        extractors.put(short.class, new ShortExtractor());
        extractors.put(Short.class, new ShortExtractor());
        extractors.put(char.class, new CharExtractor());
        extractors.put(Character.class, new CharExtractor());
        extractors.put(byte.class, new ByteExtractor());
        extractors.put(Byte.class, new ByteExtractor());
        extractors.put(boolean.class, new BooleanExtractor());
        extractors.put(Boolean.class, new BooleanExtractor());
    }

    @Override
    public List<PotentialAssignment> generate(ForAll quantifier, Class<?> type) {
        List<PotentialAssignment> assignments = new ArrayList<PotentialAssignment>();
        RandomValueExtractor<?> extractor = extractors.get(type);

        for (int i = 0, sampleSize = quantifier.sampleSize(); i < sampleSize; ++i) {
            Object nextValue = extractor.extract(random);
            assignments.add(PotentialAssignment.forValue(String.valueOf(nextValue), nextValue));
        }

        return assignments;
    }
}

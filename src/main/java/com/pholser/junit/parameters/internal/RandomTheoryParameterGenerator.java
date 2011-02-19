package com.pholser.junit.parameters.internal;

import java.util.ArrayList;
import java.util.List;

import com.pholser.junit.parameters.ForAll;
import org.junit.experimental.theories.PotentialAssignment;

public class RandomTheoryParameterGenerator implements TheoryParameterGenerator {
    private final SourceOfRandomness random;

    public RandomTheoryParameterGenerator(SourceOfRandomness random) {
        this.random = random;
    }

    @Override
    public List<PotentialAssignment> generate(ForAll quantifier, Class<?> type) {
        List<PotentialAssignment> assignments = new ArrayList<PotentialAssignment>();
        for (int i = 0; i < quantifier.sampleSize(); ++i) {
            int nextValue = random.nextInt();
            assignments.add(PotentialAssignment.forValue(Integer.toString(nextValue), nextValue));
        }
        return assignments;
    }
}

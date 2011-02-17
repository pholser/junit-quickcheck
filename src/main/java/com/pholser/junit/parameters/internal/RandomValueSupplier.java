package com.pholser.junit.parameters.internal;

import java.util.ArrayList;
import java.util.List;

import com.pholser.junit.parameters.ForAll;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParameterSupplier;
import org.junit.experimental.theories.PotentialAssignment;

public class RandomValueSupplier extends ParameterSupplier {
    @Override
    public List<PotentialAssignment> getValueSources(ParameterSignature sig) {
        List<PotentialAssignment> assignments = new ArrayList<PotentialAssignment>();
        ForAll quantifier = sig.getAnnotation(ForAll.class);
        for (int i = 0; i < quantifier.sampleSize(); ++i)
            assignments.add(PotentialAssignment.forValue(Integer.toString(i), i));
        return assignments;
    }
}

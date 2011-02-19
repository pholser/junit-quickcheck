package com.pholser.junit.parameters.internal;

import java.util.List;

import com.pholser.junit.parameters.ForAll;
import org.junit.experimental.theories.PotentialAssignment;

public interface TheoryParameterGenerator {
    List<PotentialAssignment> generate(ForAll quantifier, Class<?> type);
}

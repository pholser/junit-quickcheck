package com.pholser.junit.parameters.internal;

import java.lang.reflect.Type;
import java.util.List;

import com.pholser.junit.parameters.ForAll;
import org.junit.experimental.theories.PotentialAssignment;

public interface TheoryParameterGenerator {
    List<PotentialAssignment> generate(ForAll quantifier, Type type);
}

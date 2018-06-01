package com.pholser.junit.quickcheck.runner;

import java.util.ArrayList;
import java.util.List;

import org.junit.internal.AssumptionViolatedException;

class NoValuesSatisfiedPropertyAssumptions
    extends org.junit.AssumptionViolatedException {

    private final List<? extends AssumptionViolatedException> violations;

    NoValuesSatisfiedPropertyAssumptions(
        List<? extends AssumptionViolatedException> violations) {

        super(
            "No values satisfied property assumptions. Violated assumptions: "
                + violations);

        this.violations = new ArrayList<>(violations);
    }
}

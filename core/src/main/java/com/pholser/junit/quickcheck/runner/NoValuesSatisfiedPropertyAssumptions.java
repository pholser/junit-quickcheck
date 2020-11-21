package com.pholser.junit.quickcheck.runner;

import java.util.List;
import org.junit.internal.AssumptionViolatedException;

class NoValuesSatisfiedPropertyAssumptions
    extends org.junit.AssumptionViolatedException {

    NoValuesSatisfiedPropertyAssumptions(
        List<? extends AssumptionViolatedException> violations) {

        super(
            "No values satisfied property assumptions. Violated assumptions: "
                + violations);
    }
}

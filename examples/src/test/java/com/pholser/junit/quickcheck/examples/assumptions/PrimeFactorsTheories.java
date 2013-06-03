package com.pholser.junit.quickcheck.examples.assumptions;

import com.pholser.junit.quickcheck.ForAll;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.math.BigInteger;

import static org.hamcrest.core.IsEqual.*;
import static org.hamcrest.core.IsNot.*;
import static org.hamcrest.number.OrderingComparison.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

@RunWith(Theories.class)
public class PrimeFactorsTheories {
    @Theory public void factorsPassPrimalityTest(@ForAll int n) {
        assumeThat(n, greaterThan(0));

        for (int each : PrimeFactors.of(n))
            assertTrue(BigInteger.valueOf(each).isProbablePrime(1000));
    }

    @Theory public void factorsMultiplyToOriginal(@ForAll int n) {
        assumeThat(n, greaterThan(0));

        int product = 1;
        for (int each : PrimeFactors.of(n))
            product *= each;

        assertEquals(n, product);
    }

    @Theory public void factorizationsAreUnique(@ForAll int m, @ForAll int n) {
        assumeThat(m, greaterThan(0));
        assumeThat(n, greaterThan(0));
        assumeThat(m, not(equalTo(n)));

        assertThat(PrimeFactors.of(m), not(equalTo(PrimeFactors.of(n))));
    }
}

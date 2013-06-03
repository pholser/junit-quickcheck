package com.pholser.junit.quickcheck.examples.assumptions;

import java.util.ArrayList;
import java.util.List;

class PrimeFactors {
    static List<Integer> of(int n) {
        List<Integer> primes = new ArrayList<Integer>();

        for (int candidate = 2; candidate <= n; candidate++) {
            for (; n % candidate == 0; n /= candidate)
                primes.add(candidate);
        }

        return primes;
    }
}

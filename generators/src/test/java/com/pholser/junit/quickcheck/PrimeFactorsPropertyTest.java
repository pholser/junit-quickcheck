/*
 The MIT License

 Copyright (c) 2010-2017 Paul R. Holser, Jr.

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.pholser.junit.quickcheck;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

import static java.math.BigInteger.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

@RunWith(JUnitQuickcheck.class)
public class PrimeFactorsPropertyTest {
    static class PrimeFactors {
        static List<BigInteger> of(BigInteger n) {
            List<BigInteger> primes = new ArrayList<>();

            for (BigInteger candidate = BigInteger.valueOf(2);
                n.compareTo(ONE) > 0;
                candidate = candidate.add(ONE)) {

                for (; n.mod(candidate).equals(ZERO); n = n.divide(candidate))
                    primes.add(candidate);
            }

            return primes;
        }
    }

    @Property(trials = 7) public void factorsPassPrimalityTest(BigInteger n) {
        assumeThat(n, greaterThan(ZERO));

        for (BigInteger each : PrimeFactors.of(n))
            assertTrue(each.isProbablePrime(1000));
    }

    @Property(trials = 7) public void factorsMultiplyToOriginal(BigInteger n) {
        assumeThat(n, greaterThan(ZERO));

        BigInteger product = ONE;
        for (BigInteger each : PrimeFactors.of(n))
            product = product.multiply(each);

        assertEquals(n, product);
    }

    @Property(trials = 7) public void factorizationsAreUnique(
        BigInteger m,
        BigInteger n) {

        assumeThat(m, greaterThan(ZERO));
        assumeThat(n, greaterThan(ZERO));
        assumeThat(m, not(equalTo(n)));

        assertThat(PrimeFactors.of(m), not(equalTo(PrimeFactors.of(n))));
    }
}

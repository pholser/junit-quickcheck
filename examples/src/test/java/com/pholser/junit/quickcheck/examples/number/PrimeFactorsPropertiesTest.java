/*
 The MIT License

 Copyright (c) 2010-2021 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.examples.number;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeThat;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import java.math.BigInteger;
import org.junit.runner.RunWith;

@RunWith(JUnitQuickcheck.class)
public class PrimeFactorsPropertiesTest {
    @Property(trials = 10) public void factorsPassPrimalityTest(
        BigInteger n) {

        assumeThat(n, greaterThan(ZERO));

        for (BigInteger each : PrimeFactors.of(n))
            assertTrue(each.isProbablePrime(100));
    }

    @Property(trials = 10) public void factorsMultiplyToOriginal(
        BigInteger n) {

        assumeThat(n, greaterThan(ZERO));

        BigInteger product = ONE;
        for (BigInteger each : PrimeFactors.of(n))
            product = product.multiply(each);

        assertEquals(n, product);
    }

    @Property(trials = 10) public void factorizationsAreUnique(
        BigInteger m,
        BigInteger n) {

        assumeThat(m, greaterThan(ZERO));
        assumeThat(n, greaterThan(ZERO));
        assumeThat(m, not(equalTo(n)));

        assertThat(PrimeFactors.of(m), not(equalTo(PrimeFactors.of(n))));
    }
}

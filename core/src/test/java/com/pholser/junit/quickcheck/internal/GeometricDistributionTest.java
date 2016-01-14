/*
 The MIT License

 Copyright (c) 2010-2016 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.internal;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static java.lang.Math.ulp;
import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.*;
import static org.mockito.Mockito.when;

public class GeometricDistributionTest {
    @Rule public final ExpectedException thrown = none();
    @Rule public final MockitoRule mockito = MockitoJUnit.rule();
    @Mock private SourceOfRandomness random;

    private GeometricDistribution distro;

    @Before public void setUp() {
        distro = new GeometricDistribution();
    }

    @Test public void negativeProbability() {
        thrown.expect(IllegalArgumentException.class);

        distro.sample(-ulp(0), random);
    }

    @Test public void zeroProbability() {
        thrown.expect(IllegalArgumentException.class);

        distro.sample(0, random);
    }

    @Test public void greaterThanOneProbability() {
        thrown.expect(IllegalArgumentException.class);

        distro.sample(1 + ulp(1), random);
    }

    @Test public void sampleWithCertainProbability() {
        assertEquals(0, distro.sample(1, random));
    }

    @Test public void sampleWithNonCertainProbability() {
        when(random.nextDouble()).thenReturn(0.88);

        assertEquals(10, distro.sample(0.2, random));
    }

    @Test public void negativeMeanProbability() {
        thrown.expect(IllegalArgumentException.class);

        distro.probabilityOfMean(-ulp(0));
    }

    @Test public void zeroMeanProbability() {
        thrown.expect(IllegalArgumentException.class);

        distro.probabilityOfMean(0);
    }

    @Test public void nonZeroMeanProbability() {
        assertEquals(1 / 6D, distro.probabilityOfMean(6), 0);
    }

    @Test public void sampleWithMean() {
        when(random.nextDouble()).thenReturn(0.76);

        assertEquals(8, distro.sampleWithMean(6, random));
    }
}

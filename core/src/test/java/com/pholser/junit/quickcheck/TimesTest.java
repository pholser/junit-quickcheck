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

import java.util.Collections;

import com.pholser.junit.quickcheck.generator.Gen;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static java.util.Collections.*;
import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.*;

public class TimesTest {
    @Rule public final MockitoRule mockito = MockitoJUnit.rule();
    @Rule public final ExpectedException thrown = none();

    @Mock private SourceOfRandomness random;
    @Mock private GenerationStatus status;

    @Test public void positiveTimes() {
        Gen<Integer> g = (r, s) -> {
            assertSame(random, r);
            assertSame(status, s);
            return 12;
        };

        assertEquals(
            Collections.nCopies(10, 12),
            g.times(10).generate(random, status));
    }

    @Test public void zeroTimes() {
        assertEquals(
            emptyList(),
            Gen.pure(44).times(0).generate(random, status));
    }

    @Test public void negativeTimes() {
        thrown.expect(IllegalArgumentException.class);

        Gen.pure(2).times(-1);
    }
}

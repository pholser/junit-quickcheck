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

package com.pholser.junit.quickcheck.generator;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static com.pholser.junit.quickcheck.generator.Gen.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GenOneOfTest {
    @Rule public final MockitoRule mockito = MockitoJUnit.rule();

    @Mock private SourceOfRandomness random;

    @Test public void generatorThatPicksAnItemAtRandom() {
        when(random.nextInt(5)).thenReturn(1);

        Gen<Integer> chooser = oneOf(2, 3, 5, 7, 11);

        assertEquals(Integer.valueOf(3), chooser.generate(random, null));
    }

    @Test public void generatorThatPicksAGeneratorAtRandom() {
        when(random.nextInt(5)).thenReturn(3);

        Gen<Integer> chooser =
            oneOf(pure(2), pure(3), pure(5), pure(7), pure(11));

        assertEquals(Integer.valueOf(7), chooser.generate(random, null));
    }
}

/*
 The MIT License

 Copyright (c) 2010-2011 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.internal.random;

import java.security.SecureRandom;
import java.util.Random;

import static java.lang.Double.*;
import static java.lang.Float.*;
import static java.lang.String.*;

public class JDKSourceOfRandomness implements SourceOfRandomness {
    private final Random random;

    public JDKSourceOfRandomness() {
        this(new SecureRandom());
    }

    public JDKSourceOfRandomness(Random random) {
        this.random = random;
    }

    @Override
    public int nextInt() {
        return random.nextInt();
    }

    @Override
    public int nextInt(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException(format("bad range, {0} >= {1}", min, max));
        }
        return random.nextInt(max - min + 1) + min;
    }

    @Override
    public boolean nextBoolean() {
        return random.nextBoolean();
    }

    @Override
    public long nextLong() {
        return random.nextLong();
    }

    @Override
    public float nextFloat() {
        float result;

        do {
            result = intBitsToFloat(nextInt());
        } while (Float.isNaN(result) || Float.isInfinite(result));

        return result;
    }

    @Override
    public double nextDouble() {
        double result;

        do {
            long ell = nextLong();
            result = longBitsToDouble(ell);
        } while (Double.isNaN(result) || Double.isInfinite(result));

        return result;
    }

    @Override
    public byte[] nextBytes(int count) {
        byte[] buffer = new byte[count];
        random.nextBytes(buffer);
        return buffer;
    }
}

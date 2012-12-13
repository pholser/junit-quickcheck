/*
 The MIT License

 Copyright (c) 2010-2012 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.random;

import java.util.Random;

import static java.lang.String.*;

public class SourceOfRandomness {
    private final Random delegate;

    public SourceOfRandomness(Random delegate) {
        this.delegate = delegate;
    }

    public boolean nextBoolean() {
        return delegate.nextBoolean();
    }

    public void nextBytes(byte[] bytes) {
        delegate.nextBytes(bytes);
    }

    public double nextDouble() {
        return delegate.nextDouble();
    }

    public float nextFloat() {
        return delegate.nextFloat();
    }

    public double nextGaussian() {
        return delegate.nextGaussian();
    }

    public int nextInt() {
        return delegate.nextInt();
    }

    public int nextInt(int n) {
        return delegate.nextInt(n);
    }

    public long nextLong() {
        return delegate.nextLong();
    }

    public void setSeed(long seed) {
        delegate.setSeed(seed);
    }

    public int nextInt(int min, int max) {
        if (min > max)
            throw new IllegalArgumentException(format("bad range, %d > %d", min, max));

        if (min == max)
            return min;
        int nextRandom = delegate.nextInt(max - min + 1);
        return nextRandom + min;
    }

    public byte[] nextBytes(int count) {
        byte[] buffer = new byte[count];
        delegate.nextBytes(buffer);
        return buffer;
    }
}

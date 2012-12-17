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

import java.math.BigInteger;
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

    public byte nextByte(byte min, byte max) {
        return (byte) nextLong(min, max);
    }

    public short nextShort(short min, short max) {
        return (short) nextLong(min, max);
    }

    public char nextChar(char min, char max) {
        checkRange(min > max, "bad range, %c > %c", min, max);

        return (char) nextLong(min, max);
    }

    public int nextInt(int min, int max) {
        return (int) nextLong(min, max);
    }

    public long nextLong(long min, long max) {
        checkRange(min > max, "bad range, %d > %d", min, max);

        long range = max - min + 1;
        return min + (long) (range * delegate.nextDouble());
    }

    public float nextFloat(float min, float max) {
        return (float) nextDouble(min, max);
    }

    public double nextDouble(double min, double max) {
        int comparison = Double.compare(min, max);

        checkRange(comparison > 0, "bad range, %f > %f", min, max);
        if (comparison == 0)
            return min;

        return min + (max - min) * delegate.nextDouble();
    }

    public byte[] nextBytes(int count) {
        byte[] buffer = new byte[count];
        delegate.nextBytes(buffer);
        return buffer;
    }

    public BigInteger nextBigInteger(int numberOfBits) {
        return new BigInteger(numberOfBits, delegate);
    }

    private void checkRange(boolean condition, String pattern, Object min, Object max) {
        if (condition)
            throw new IllegalArgumentException(format(pattern, min, max));
    }
}

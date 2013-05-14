/*
 The MIT License

 Copyright (c) 2010-2013 Paul R. Holser, Jr.

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

import com.pholser.junit.quickcheck.internal.Ranges;

import static com.pholser.junit.quickcheck.internal.Ranges.*;

/**
 * A source of randomness, fed to {@link com.pholser.junit.quickcheck.generator.Generator}s so they can produce
 * random values for theory parameters.
 */
public class SourceOfRandomness {
    private final Random delegate;

    /**
     * Makes a new source of randomness.
     *
     * @param delegate a JDK source of randomness, to which the new instance will delegate
     */
    public SourceOfRandomness(Random delegate) {
        this.delegate = delegate;
    }

    /**
     * @see java.util.Random#nextBoolean()
     */
    public boolean nextBoolean() {
        return delegate.nextBoolean();
    }

    /**
     * @see java.util.Random#nextBytes(byte[])
     */
    public void nextBytes(byte[] bytes) {
        delegate.nextBytes(bytes);
    }

    /**
     * @see java.util.Random#nextDouble()
     */
    public double nextDouble() {
        return delegate.nextDouble();
    }

    /**
     * @see java.util.Random#nextFloat()
     */
    public float nextFloat() {
        return delegate.nextFloat();
    }

    /**
     * @see java.util.Random#nextGaussian()
     */
    public double nextGaussian() {
        return delegate.nextGaussian();
    }

    /**
     * @see java.util.Random#nextInt()
     */
    public int nextInt() {
        return delegate.nextInt();
    }

    /**
     * @see java.util.Random#nextInt(int)
     */
    public int nextInt(int n) {
        return delegate.nextInt(n);
    }

    /**
     * @see java.util.Random#nextLong()
     */
    public long nextLong() {
        return delegate.nextLong();
    }

    /**
     * @see java.util.Random#setSeed(long)
     */
    public void setSeed(long seed) {
        delegate.setSeed(seed);
    }

    /**
     * Gives a random {@code byte} value {@code v} such that {@code min <= v <= max}.
     *
     * @param min lower bound of the desired range
     * @param max upper bound of the desired range
     * @return a random value
     */
    public byte nextByte(byte min, byte max) {
        return (byte) nextLong(min, max);
    }

    /**
     * Gives a random {@code short} value {@code v} such that {@code min <= v <= max}.
     *
     * @param min lower bound of the desired range
     * @param max upper bound of the desired range
     * @return a random value
     */
    public short nextShort(short min, short max) {
        return (short) nextLong(min, max);
    }

    /**
     * Gives a random {@code char} value {@code v} such that {@code min <= v <= max}.
     *
     * @param min lower bound of the desired range
     * @param max upper bound of the desired range
     * @return a random value
     */
    public char nextChar(char min, char max) {
        checkRange("c", min, max);

        return (char) nextLong(min, max);
    }

    /**
     * Gives a random {@code int} value {@code v} such that {@code min <= v <= max}.
     *
     * @param min lower bound of the desired range
     * @param max upper bound of the desired range
     * @return a random value
     */
    public int nextInt(int min, int max) {
        return (int) nextLong(min, max);
    }

    /**
     * Gives a random {@code long} value {@code v} such that {@code min <= v <= max}.
     *
     * @param min lower bound of the desired range
     * @param max upper bound of the desired range
     * @return a random value
     */
    public long nextLong(long min, long max) {
        int comparison = checkRange("d", min, max);
        if (comparison == 0)
            return min;

        return Ranges.randomBigIntegerInRange(this, BigInteger.valueOf(min), BigInteger.valueOf(max)).longValue();
    }

    /**
     * Gives a random {@code float} value {@code v} such that {@code min <= v < max}.
     *
     * @param min lower bound of the desired range
     * @param max upper bound of the desired range
     * @return a random value
     */
    public float nextFloat(float min, float max) {
        int comparison = checkRange("f", min, max);
        return comparison == 0 ? min : min + (max - min) * delegate.nextFloat();
    }

    /**
     * Gives a random {@code double} value {@code v} such that {@code min <= v < max}.
     *
     * @param min lower bound of the desired range
     * @param max upper bound of the desired range
     * @return a random value
     */
    public double nextDouble(double min, double max) {
        int comparison = checkRange("f", min, max);
        return comparison == 0 ? min : min + (max - min) * delegate.nextDouble();
    }

    /**
     * Gives an array of a given length containing random bytes.
     *
     * @param count the desired length of the random byte array
     * @return random bytes
     * @see java.util.Random#nextBytes(byte[])
     */
    public byte[] nextBytes(int count) {
        byte[] buffer = new byte[count];
        delegate.nextBytes(buffer);
        return buffer;
    }

    /**
     * Gives a random {@code BigInteger} representable by the given number of bits.
     *
     * @param numberOfBits the desired number of bits
     * @return a random {@code BigInteger}
     * @see BigInteger#BigInteger(int, java.util.Random)
     */
    public BigInteger nextBigInteger(int numberOfBits) {
        return new BigInteger(numberOfBits, delegate);
    }
}

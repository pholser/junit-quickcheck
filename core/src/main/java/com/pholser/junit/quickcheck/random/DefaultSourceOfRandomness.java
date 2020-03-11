/*
 The MIT License

 Copyright (c) 2010-2020 Paul R. Holser, Jr.

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Random;

import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.Items;
import com.pholser.junit.quickcheck.internal.Ranges;

import static java.util.concurrent.TimeUnit.*;

import static com.pholser.junit.quickcheck.internal.Ranges.*;

/**
 * Default implementation of {@link SourceOfRandomness}.
 */
public class DefaultSourceOfRandomness implements SourceOfRandomness {
    private static final BigInteger NANOS_PER_SECOND =
        BigInteger.valueOf(SECONDS.toNanos(1));

    private final Random delegate;

    private long seed;

    /**
     * Makes a new source of randomness.
     *
     * @param delegate a JDK source of randomness, to which the new instance
     * will delegate
     */
    public DefaultSourceOfRandomness(Random delegate) {
        seed = delegate.nextLong();
        this.delegate = delegate;
        delegate.setSeed(seed);
    }

    @Override
    public Random toJDKRandom() {
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();

        try (ObjectOutputStream objectOut = new ObjectOutputStream(bytesOut)) {
            objectOut.writeObject(delegate);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }

        ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesOut.toByteArray());
        try (ObjectInputStream objectIn = new ObjectInputStream(bytesIn)) {
            return (Random) objectIn.readObject();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        } catch (ClassNotFoundException shouldNeverHappen) {
            throw new AssertionError(shouldNeverHappen);
        }
    }

    @Override
    public boolean nextBoolean() {
        return delegate.nextBoolean();
    }

    @Override
    public void nextBytes(byte[] bytes) {
        delegate.nextBytes(bytes);
    }

    @Override
    public byte[] nextBytes(int count) {
        byte[] buffer = new byte[count];
        delegate.nextBytes(buffer);
        return buffer;
    }

    @Override
    public double nextDouble() {
        return delegate.nextDouble();
    }

    @Override
    public float nextFloat() {
        return delegate.nextFloat();
    }

    @Override
    public double nextGaussian() {
        return delegate.nextGaussian();
    }

    @Override
    public int nextInt() {
        return delegate.nextInt();
    }

    @Override
    public int nextInt(int n) {
        return delegate.nextInt(n);
    }

    @Override
    public long nextLong() {
        return delegate.nextLong();
    }

    @Override
    public void setSeed(long seed) {
        this.seed = seed;
        delegate.setSeed(seed);
    }

    @Override
    public long seed() {
        return seed;
    }

    @Override
    public byte nextByte(byte min, byte max) {
        return (byte) nextLong(min, max);
    }

    @Override
    public char nextChar(char min, char max) {
        checkRange(Ranges.Type.CHARACTER, min, max);

        return (char) nextLong(min, max);
    }

    @Override
    public double nextDouble(double min, double max) {
        int comparison = checkRange(Ranges.Type.FLOAT, min, max);
        return comparison == 0 ? min : min + (max - min) * nextDouble();
    }

    @Override
    public float nextFloat(float min, float max) {
        int comparison = checkRange(Ranges.Type.FLOAT, min, max);
        return comparison == 0 ? min : min + (max - min) * nextFloat();
    }

    @Override
    public int nextInt(int min, int max) {
        return (int) nextLong(min, max);
    }

    @Override
    public long nextLong(long min, long max) {
        int comparison = checkRange(Ranges.Type.INTEGRAL, min, max);
        if (comparison == 0)
            return min;

        return Ranges.choose(this, min, max);
    }

    @Override
    public short nextShort(short min, short max) {
        return (short) nextLong(min, max);
    }

    @Override
    public BigInteger nextBigInteger(int numberOfBits) {
        return new BigInteger(numberOfBits, delegate);
    }

    @Override
    public Instant nextInstant(Instant min, Instant max) {
        int comparison = checkRange(Ranges.Type.STRING, min, max);
        if (comparison == 0)
            return min;

        long[] next = nextSecondsAndNanos(
            min.getEpochSecond(),
            min.getNano(),
            max.getEpochSecond(),
            max.getNano());

        return Instant.ofEpochSecond(next[0], next[1]);
    }

    @Override
    public Duration nextDuration(Duration min, Duration max) {
        int comparison = checkRange(Ranges.Type.STRING, min, max);
        if (comparison == 0)
            return min;

        long[] next = nextSecondsAndNanos(
            min.getSeconds(),
            min.getNano(),
            max.getSeconds(),
            max.getNano());

        return Duration.ofSeconds(next[0], next[1]);
    }

    @Override
    public <T> T choose(Collection<T> items) {
        return Items.choose(items, this);
    }

    @Override
    public <T> T choose(T[] items) {
        return items[nextInt(items.length)];
    }

    private long[] nextSecondsAndNanos(
        long minSeconds,
        long minNanos,
        long maxSeconds,
        long maxNanos) {

        BigInteger nanoMin = BigInteger.valueOf(minSeconds)
            .multiply(NANOS_PER_SECOND)
            .add(BigInteger.valueOf(minNanos));
        BigInteger nanoMax = BigInteger.valueOf(maxSeconds)
            .multiply(NANOS_PER_SECOND)
            .add(BigInteger.valueOf(maxNanos));

        BigInteger[] generated = Ranges.choose(this, nanoMin, nanoMax)
            .divideAndRemainder(NANOS_PER_SECOND);

        return new long[] { generated[0].longValue(), generated[1].longValue() };
    }
}

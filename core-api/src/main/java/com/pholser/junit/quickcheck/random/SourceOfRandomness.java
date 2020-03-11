package com.pholser.junit.quickcheck.random;

import com.pholser.junit.quickcheck.generator.Generator;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Random;

/**
 * A source of randomness, fed to
 * {@linkplain Generator generators}
 * so they can produce random values for property parameters.
 */
public interface SourceOfRandomness {
    /**
     * <p>Gives a JDK source of randomness, with the same internal state as
     * this source of randomness.</p>
     *
     * @return a JDK "clone" of self
     */
    Random toJDKRandom();

    /**
     * @return a uniformly distributed boolean value
     * @see Random#nextBoolean()
     */
    boolean nextBoolean();

    /**
     * @param bytes a byte array to fill with random values
     * @see Random#nextBytes(byte[])
     */
    void nextBytes(byte[] bytes);

    /**
     * Gives an array of a given length containing random bytes.
     *
     * @param count the desired length of the random byte array
     * @return random bytes
     * @see Random#nextBytes(byte[])
     */
    byte[] nextBytes(int count);

    /**
     * @return a uniformly distributed random {@code double} value in the
     * interval {@code [0.0, 1.0)}
     * @see Random#nextDouble()
     */
    double nextDouble();

    /**
     * @return a uniformly distributed random {@code float} value in the
     * interval {@code [0.0, 1.0)}
     * @see Random#nextFloat()
     */
    float nextFloat();

    /**
     * @return a Gaussian-distributed random double value
     * @see Random#nextGaussian()
     */
    double nextGaussian();

    /**
     * @return a uniformly distributed random {@code int} value
     * @see Random#nextInt()
     */
    int nextInt();

    /**
     * @param n upper bound
     * @return a uniformly distributed random {@code int} value in the interval
     * {@code [0, n)}
     * @see Random#nextInt(int)
     */
    int nextInt(int n);

    /**
     * @return a uniformly distributed random {@code long} value
     * @see Random#nextLong()
     */
    long nextLong();

    /**
     * @param seed value with which to seed this source of randomness
     * @see Random#setSeed(long)
     */
    void setSeed(long seed);

    /**
     * @return the value used to initially seed this source of randomness
     */
    long seed();

    /**
     * Gives a random {@code byte} value, uniformly distributed across the
     * interval {@code [min, max]}.
     *
     * @param min lower bound of the desired interval
     * @param max upper bound of the desired interval
     * @return a random value
     */
    byte nextByte(byte min, byte max);

    /**
     * Gives a random {@code char} value, uniformly distributed across the
     * interval {@code [min, max]}.
     *
     * @param min lower bound of the desired interval
     * @param max upper bound of the desired interval
     * @return a random value
     */
    char nextChar(char min, char max);

    /**
     * <p>Gives a random {@code double} value in the interval
     * {@code [min, max)}.</p>
     *
     * <p>This naive implementation takes a random {@code double} value from
     * {@link Random#nextDouble()} and scales/shifts the value into the desired
     * interval. This may give surprising results for large ranges.</p>
     *
     * @param min lower bound of the desired interval
     * @param max upper bound of the desired interval
     * @return a random value
     */
    double nextDouble(double min, double max);

    /**
     * <p>Gives a random {@code float} value in the interval
     * {@code [min, max)}.</p>
     *
     * <p>This naive implementation takes a random {@code float} value from
     * {@link Random#nextFloat()} and scales/shifts the value into the desired
     * interval. This may give surprising results for large ranges.</p>
     *
     * @param min lower bound of the desired interval
     * @param max upper bound of the desired interval
     * @return a random value
     */
    float nextFloat(float min, float max);

    /**
     * Gives a random {@code int} value, uniformly distributed across the
     * interval {@code [min, max]}.
     *
     * @param min lower bound of the desired interval
     * @param max upper bound of the desired interval
     * @return a random value
     */
    int nextInt(int min, int max);

    /**
     * Gives a random {@code long} value, uniformly distributed across the
     * interval {@code [min, max]}.
     *
     * @param min lower bound of the desired interval
     * @param max upper bound of the desired interval
     * @return a random value
     */
    long nextLong(long min, long max);

    /**
     * Gives a random {@code short} value, uniformly distributed across the
     * interval {@code [min, max]}.
     *
     * @param min lower bound of the desired interval
     * @param max upper bound of the desired interval
     * @return a random value
     */
    short nextShort(short min, short max);

    /**
     * Gives a random {@code BigInteger} representable by the given number
     * of bits.
     *
     * @param numberOfBits the desired number of bits
     * @return a random {@code BigInteger}
     * @see BigInteger#BigInteger(int, Random)
     */
    BigInteger nextBigInteger(int numberOfBits);

    /**
     * Gives a random {@code Instant} value, uniformly distributed across the
     * interval {@code [min, max]}.
     *
     * @param min lower bound of the desired interval
     * @param max upper bound of the desired interval
     * @return a random value
     */
    Instant nextInstant(Instant min, Instant max);

    /**
     * Gives a random {@code Duration} value, uniformly distributed across the
     * interval {@code [min, max]}.
     *
     * @param min lower bound of the desired interval
     * @param max upper bound of the desired interval
     * @return a random value
     */
    Duration nextDuration(Duration min, Duration max);

    /**
     * Gives a random element of the given collection.
     *
     * @param <T> type of items in the collection
     * @param items a collection
     * @return a randomly chosen element from the collection
     */
    <T> T choose(Collection<T> items);

    /**
     * Gives a random element of the given array.
     *
     * @param <T> type of items in the array
     * @param items an array
     * @return a randomly chosen element from the array
     */
    <T> T choose(T[] items);
}

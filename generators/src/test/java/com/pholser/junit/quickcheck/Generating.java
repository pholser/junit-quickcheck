package com.pholser.junit.quickcheck;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.mockito.verification.VerificationMode;

import static com.pholser.junit.quickcheck.generator.RangeAttributes.*;
import static org.mockito.Mockito.*;

public class Generating {
    private Generating() {
        throw new UnsupportedOperationException();
    }

    public static boolean booleans(SourceOfRandomness random) {
        return random.nextBoolean();
    }

    public static void verifyBooleans(SourceOfRandomness random, VerificationMode mode) {
        verify(random, mode).nextBoolean();
    }

    public static byte bytes(SourceOfRandomness random) {
        return random.nextByte(minByte(), maxByte());
    }

    public static void verifyBytes(SourceOfRandomness random, VerificationMode mode) {
        verify(random, mode).nextByte(minByte(), maxByte());
    }

    public static char chars(SourceOfRandomness random) {
        return random.nextChar(minChar(), maxChar());
    }

    public static void verifyChars(SourceOfRandomness random, VerificationMode mode) {
        verify(random, mode).nextChar(minChar(), maxChar());
    }

    public static int charsForString(SourceOfRandomness random) {
        return random.nextInt(0, 0xD7FF);
    }

    public static void verifyCharsForString(SourceOfRandomness random, VerificationMode mode) {
        verify(random, mode).nextInt(0, 0xD7FF);
    }

    public static double doubles(SourceOfRandomness random) {
        return random.nextDouble(minDouble(), maxDouble());
    }

    public static void verifyDoubles(SourceOfRandomness random, VerificationMode mode) {
        verify(random, mode).nextDouble(minDouble(), maxDouble());
    }

    public static float floats(SourceOfRandomness random) {
        return random.nextFloat(minFloat(), maxFloat());
    }

    public static void verifyFloats(SourceOfRandomness random, VerificationMode mode) {
        verify(random, mode).nextFloat(minFloat(), maxFloat());
    }

    public static int ints(SourceOfRandomness random) {
        return ints(random, minInt(), maxInt());
    }

    public static void verifyInts(SourceOfRandomness random, VerificationMode mode) {
        verifyInts(random, mode, minInt(), maxInt());
    }

    public static int ints(SourceOfRandomness random, int min, int max) {
        return random.nextInt(min, max);
    }

    public static void verifyInts(SourceOfRandomness random, VerificationMode mode, int min, int max) {
        verify(random, mode).nextInt(min, max);
    }

    public static long longs(SourceOfRandomness random) {
        return longs(random, minLong(), maxLong());
    }

    public static void verifyLongs(SourceOfRandomness random, VerificationMode mode) {
        verifyLongs(random, mode, minLong(), maxLong());
    }

    public static long longs(SourceOfRandomness random, long min, long max) {
        return random.nextLong(min, max);
    }

    public static void verifyLongs(SourceOfRandomness random, VerificationMode mode, long min, long max) {
        verify(random, mode).nextLong(min, max);
    }
}

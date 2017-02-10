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

import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.mockito.verification.VerificationMode;

import static com.pholser.junit.quickcheck.generator.RangeAttributes.*;
import static org.mockito.Mockito.*;

public final class Generating {
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

    public static int ints(SourceOfRandomness random, int n) {
        return random.nextInt(n);
    }

    public static void verifyInts(SourceOfRandomness random, VerificationMode mode, int n) {
        verify(random, mode).nextInt(n);
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

    public static short shorts(SourceOfRandomness random) {
        return random.nextShort(minShort(), maxShort());
    }

    public static void verifyShorts(SourceOfRandomness random, VerificationMode mode) {
        verify(random, mode).nextShort(minShort(), maxShort());
    }
}

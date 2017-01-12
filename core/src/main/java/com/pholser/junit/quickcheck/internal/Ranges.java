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

package com.pholser.junit.quickcheck.internal;

import java.math.BigInteger;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static com.pholser.junit.quickcheck.internal.Ranges.Type.*;
import static java.lang.String.*;

public final class Ranges {
    public enum Type {
        CHARACTER("c"),
        INTEGRAL("d"),
        FLOAT("f"),
        STRING("s");

        private final String pattern;

        Type(String pattern) {
            this.pattern = pattern;
        }
    }

    private Ranges() {
        throw new UnsupportedOperationException();
    }

    public static <T extends Comparable<? super T>> int checkRange(
        Type type,
        T min,
        T max) {

        int comparison = min.compareTo(max);
        if (comparison > 0) {
            throw new IllegalArgumentException(
                format(
                    "bad range, %" + type.pattern + " > %" + type.pattern,
                    min,
                    max));
        }

        return comparison;
    }

    public static BigInteger choose(
        SourceOfRandomness random,
        BigInteger min,
        BigInteger max) {

        BigInteger range = max.subtract(min).add(BigInteger.ONE);
        BigInteger generated;

        do {
            generated = random.nextBigInteger(range.bitLength());
        } while (generated.compareTo(range) >= 0);

        return generated.add(min);
    }

    public static long choose(SourceOfRandomness random, long min, long max) {
        checkRange(INTEGRAL, min, max);

        /* There are some edges cases with integer overflows, for instance,
           when (max - min) exceeds Long.MAX_VALUE. These cases should be
           relatively rare under the assumption that choosing
           [Long.MIN_VALUE, Long.MAX_VALUE] can be simplified to choosing any
           random long. Thus, the optimization here only deals with the common
           situation that no overflows are possible (maybe the heuristic to
           detect that could be improved).
         */
        boolean noOverflowIssues =
            max < ((long) 1 << 62) && min > -(((long) 1) << 62);

        if (noOverflowIssues) {
            // fast path: use long computations
            long range = (max - min) + 1;
            long mask = findNextPowerOfTwoLong(range) - 1;

            // loop to avoid distribution bias (as would be the case
            // with modulo division)
            long generated;
            do {
                generated = Math.abs(random.nextLong()) & mask;
            } while (generated >= range);

            return generated + min;
        } else {
            // slow path: fall back to BigInteger to avoid any surprises
            return choose(
                random,
                BigInteger.valueOf(min),
                BigInteger.valueOf(max))
                .longValue();
        }
    }

    static long findNextPowerOfTwoLong(long positiveLong) {
        return isPowerOfTwoLong(positiveLong)
            ? positiveLong
            : ((long) 1) << (64 - Long.numberOfLeadingZeros(positiveLong));
    }

    private static boolean isPowerOfTwoLong(long positiveLong) {
        return (positiveLong & (positiveLong - 1)) == 0;
    }
}

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

package com.pholser.junit.quickcheck.generator;

import java.math.BigInteger;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class BigIntegerGenerator extends Generator<BigInteger> {
    private BigInteger min;
    private BigInteger max;

    public BigIntegerGenerator() {
        super(BigInteger.class);
    }

    public void configure(InRange range) {
        min = new BigInteger(range.min());
        max = new BigInteger(range.max());
    }

    // TODO: missing min or max == +/- 10^size, or opposite +/- that if it would make the range illegal
    // TODO: range check

    @Override
    public BigInteger generate(SourceOfRandomness random, GenerationStatus status) {
        if (min != null && max != null) {
            BigInteger range = max.subtract(min);
            BigInteger generated;

            do {
                generated = random.nextBigInteger(range.bitLength());
            } while (generated.compareTo(range) >= 0);

            return generated.add(min);
        }

        return random.nextBigInteger(status.size() + 1);
    }
}

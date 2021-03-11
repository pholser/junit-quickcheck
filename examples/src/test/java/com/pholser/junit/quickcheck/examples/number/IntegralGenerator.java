/*
 The MIT License

 Copyright (c) 2010-2021 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.examples.number;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import java.util.Arrays;

public class IntegralGenerator extends Generator<Integer> {
    private NonNegative nonNegative;

    public IntegralGenerator() {
        super(Arrays.asList(Integer.class, int.class));
    }

    @Override public Integer generate(
        SourceOfRandomness random,
        GenerationStatus status) {

        int value = random.nextInt();
        return nonNegative != null ? abs(value) : value;
    }

    public void configure(NonNegative nonNegative) {
        this.nonNegative = nonNegative;
    }

    // Ever-so-slightly favors +(MIN_VALUE + 1).
    // Math.abs(MIN_VALUE) is negative.
    private static int abs(int i) {
        return i == Integer.MIN_VALUE
            ? Math.abs(i + 1)
            : Math.abs(i);
    }
}

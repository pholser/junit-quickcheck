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

import com.pholser.junit.quickcheck.internal.ParameterSampler;
import com.pholser.junit.quickcheck.runner.sampling.ExhaustiveParameterSampler;
import com.pholser.junit.quickcheck.runner.sampling.TupleParameterSampler;

/**
 * Represents different modes of execution of property-based tests.
 *
 * @see com.pholser.junit.quickcheck.generator.Only
 * @see com.pholser.junit.quickcheck.generator.Also
 */
public enum Mode {
    /**
     * Verify {@link Property#trials()} tuples of arguments for a property's
     * parameters.
     */
    SAMPLING {
        @Override ParameterSampler sampler(int defaultSampleSize) {
            return new TupleParameterSampler(defaultSampleSize);
        }
    },

    /**
     * Generate {@link Property#trials()} arguments for each parameter
     * property, and verify the cross-products of those sets of arguments.
     * This behavior mirrors that of the JUnit
     * {@link org.junit.experimental.theories.Theories} runner.
     */
    EXHAUSTIVE {
        @Override ParameterSampler sampler(int defaultSampleSize) {
            return new ExhaustiveParameterSampler(defaultSampleSize);
        }
    };

    abstract ParameterSampler sampler(int defaultSampleSize);
}

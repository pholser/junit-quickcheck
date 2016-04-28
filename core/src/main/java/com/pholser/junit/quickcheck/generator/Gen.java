/*
 The MIT License

 Copyright (c) 2010-2016 Paul R. Holser, Jr.

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

import java.util.function.Function;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

@FunctionalInterface
public interface Gen<T> {
    /**
     * <p>Produces a value for a property parameter.</p>
     *
     * <p>A generator may raise an unchecked exception if some condition exists
     * which would lead to a confusing generation -- for example, if a
     * generator honored a "range" configuration, and the endpoints were
     * transposed.</p>
     *
     * @param random source of randomness to be used when generating the value
     * @param status an object that the generator can use to influence the
     * value it produces. For example, a generator for lists can use the
     * {@link GenerationStatus#size() size} method to generate lists with a
     * given number of elements.
     * @return the generated value
     */
    T generate(SourceOfRandomness random, GenerationStatus status);

    default <U> Gen<U> map(Function<? super T, ? extends U> f) {
        return (random, status) -> f.apply(Gen.this.generate(random, status));
    }

    default <U> Gen<U> flatMap(Function<? super T, Gen<? extends U>> f) {
        return (random, status) -> f.apply(Gen.this.generate(random, status)).generate(random, status);
    }
}

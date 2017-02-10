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

package com.pholser.junit.quickcheck.internal.generator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.internal.GeometricDistribution;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

abstract class AbstractGenerationStatus implements GenerationStatus {
    private final GeometricDistribution distro;
    private final SourceOfRandomness random;
    private final Map<Key<?>, Object> contextValues = new HashMap<>();

    AbstractGenerationStatus(
        GeometricDistribution distro,
        SourceOfRandomness random) {

        this.distro = distro;
        this.random = random;
    }

    @Override public <T> GenerationStatus setValue(Key<T> key, T value) {
        contextValues.put(key, value);
        return this;
    }

    @Override public <T> Optional<T> valueOf(Key<T> key) {
        return Optional.ofNullable(key.cast(contextValues.get(key)));
    }

    @Override public int size() {
        return distro.sampleWithMean(attempts() + 1, random);
    }

    protected final SourceOfRandomness random() {
        return random;
    }
}

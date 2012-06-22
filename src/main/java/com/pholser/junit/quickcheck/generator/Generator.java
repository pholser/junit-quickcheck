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

import java.util.ArrayList;
import java.util.List;

import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;

import static java.util.Arrays.*;
import static java.util.Collections.*;

/**
 * Produces values for theory parameters.
 *
 * @param <T> type of theory parameter to apply this generator's values to
 */
public abstract class Generator<T> {
    private final List<Class<T>> types = new ArrayList<Class<T>>();

    /**
     * @param type class token for type of theory parameter this generator is applicable to
     */
    @SuppressWarnings("unchecked")
    protected Generator(Class<T> type) {
        this(asList(type));
    }

    /**
     * Used for generators of primitives/their wrappers. For example, a {@code Generator<Integer>} can be used for
     * theory parameters of type {@code Integer} or {@code int}.
     *
     * @param types class tokens for type of theory parameter this generator is applicable to
     */
    protected Generator(List<Class<T>> types) {
        this.types.addAll(types);
    }

    /**
     * @return class tokens for the types of theory parameters this generator is applicable to
     */
    public List<Class<T>> types() {
        return unmodifiableList(types);
    }

    /**
     * Produces a value for a theory parameter.
     *
     * @param random a source of randomness to be used when generating the value.
     * @param size a non-negative value that the generator can use to influence the magnitude of the generated value,
     * if desired.
     * @return the generated value
     */
    public abstract T generate(SourceOfRandomness random, int size);

    /**
     * @return whether this generator has component generators, such as for those generators that produce lists or
     * arrays.
     */
    public boolean hasComponents() {
        return false;
    }

    public int numberOfNeededComponents() {
        return 0;
    }

    /**
     * Adds component generators to this generator.
     *
     * @param componentGenerators component generators to add
     */
    public void addComponentGenerators(List<Generator<?>> componentGenerators) {
        // do nothing by default
    }
}

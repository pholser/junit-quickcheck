/*
 The MIT License

 Copyright (c) 2010-2015 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.generator.java.util;

import java.util.Collection;

import com.pholser.junit.quickcheck.generator.ComponentizedGenerator;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static com.pholser.junit.quickcheck.internal.Ranges.Type.INTEGRAL;
import static com.pholser.junit.quickcheck.internal.Ranges.checkRange;

/**
 * <p>Base class for generators of {@link Collection}s, such as {@link java.util.List}s and {@link java.util.Set}.</p>
 *
 * <p>The generated collection has a number of elements limited by {@link GenerationStatus#size()}, or else
 * by the attributes of a {@link Size} marking. The individual elements will have a type corresponding to the
 * theory parameter's type argument.</p>
 *
 * @param <T> the type of collection generated
 */
public abstract class CollectionGenerator<T extends Collection> extends ComponentizedGenerator<T> {
    private Size sizeRange;

    protected CollectionGenerator(Class<T> type) {
        super(type);
    }

    /**
     * Tells this generator to produce values with a number of elements within a specified minimum and/or maximum,
     * inclusive, chosen with uniform distribution.
     *
     * @param sizeRange annotation that gives the size constraints
     */
    public void configure(Size sizeRange) {
        this.sizeRange = sizeRange;
        checkRange(INTEGRAL, sizeRange.min(), sizeRange.max());
    }

    @SuppressWarnings("unchecked")
    @Override public T generate(SourceOfRandomness random, GenerationStatus status) {
        int size = size(random, status);

        T items = empty();
        for (int i = 0; i < size; ++i)
            items.add(componentGenerators().get(0).generate(random, status));

        return items;
    }

    @Override public int numberOfNeededComponents() {
        return 1;
    }

    @Override public boolean canRegisterAsType(Class<?> type) {
        return !Object.class.equals(type);
    }

    protected abstract T empty();

    private int size(SourceOfRandomness random, GenerationStatus status) {
        return sizeRange != null
            ? random.nextInt(sizeRange.min(), sizeRange.max())
            : status.size();
    }
}

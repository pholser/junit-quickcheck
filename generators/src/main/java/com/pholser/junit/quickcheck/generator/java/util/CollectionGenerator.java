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

package com.pholser.junit.quickcheck.generator.java.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.pholser.junit.quickcheck.generator.ComponentizedGenerator;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Shrink;
import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.util.stream.StreamSupport.*;

import static com.pholser.junit.quickcheck.internal.Lists.*;
import static com.pholser.junit.quickcheck.internal.Ranges.*;
import static com.pholser.junit.quickcheck.internal.Ranges.Type.*;
import static com.pholser.junit.quickcheck.internal.Reflection.*;
import static com.pholser.junit.quickcheck.internal.Sequences.*;

/**
 * <p>Base class for generators of {@link Collection}s.</p>
 *
 * <p>The generated collection has a number of elements limited by
 * {@link GenerationStatus#size()}, or else by the attributes of a {@link Size}
 * marking. The individual elements will have a type corresponding to the
 * collection's type argument.</p>
 *
 * @param <T> the type of collection generated
 */
public abstract class CollectionGenerator<T extends Collection>
    extends ComponentizedGenerator<T> {

    private Size sizeRange;

    protected CollectionGenerator(Class<T> type) {
        super(type);
    }

    /**
     * <p>Tells this generator to add elements to the generated collection
     * a number of times within a specified minimum and/or maximum, inclusive,
     * chosen with uniform distribution.</p>
     *
     * <p>Note that some kinds of collections disallow duplicates, so the
     * number of elements added may not be equal to the collection's
     * {@link Collection#size()}.</p>
     *
     * @param size annotation that gives the size constraints
     */
    public void configure(Size size) {
        this.sizeRange = size;
        checkRange(INTEGRAL, size.min(), size.max());
    }

    @SuppressWarnings("unchecked")
    @Override public T generate(SourceOfRandomness random, GenerationStatus status) {
        int size = size(random, status);

        T items = empty();
        for (int i = 0; i < size; ++i)
            items.add(componentGenerators().get(0).generate(random, status));

        return items;
    }

    @Override public List<T> doShrink(SourceOfRandomness random, T larger) {
        @SuppressWarnings("unchecked")
        List<Object> asList = new ArrayList<>(larger);

        List<T> shrinks = new ArrayList<>();
        shrinks.addAll(removals(asList));

        @SuppressWarnings("unchecked")
        Shrink<Object> generator = (Shrink<Object>) componentGenerators().get(0);

        List<List<Object>> oneItemShrinks = shrinksOfOneItem(random, asList, generator);
        shrinks.addAll(oneItemShrinks.stream()
            .map(this::convert)
            .filter(this::inSizeRange)
            .collect(Collectors.toList()));

        return shrinks;
    }

    @Override public int numberOfNeededComponents() {
        return 1;
    }

    protected final T empty() {
        return instantiate(findConstructor(types().get(0)));
    }

    private boolean inSizeRange(T items) {
        return sizeRange == null
            || (items.size() >= sizeRange.min() && items.size() <= sizeRange.max());
    }

    private int size(SourceOfRandomness random, GenerationStatus status) {
        return sizeRange != null
            ? random.nextInt(sizeRange.min(), sizeRange.max())
            : status.size();
    }

    private List<T> removals(List<?> items) {
        return stream(halving(items.size()).spliterator(), false)
            .map(i -> removeFrom(items, i))
            .flatMap(Collection::stream)
            .map(this::convert)
            .filter(this::inSizeRange)
            .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private T convert(List<?> items) {
        T converted = empty();
        converted.addAll(items);
        return converted;
    }
}

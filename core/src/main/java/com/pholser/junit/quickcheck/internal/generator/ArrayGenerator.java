/*
 The MIT License

 Copyright (c) 2010-2020 Paul R. Holser, Jr.

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

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.Shrink;
import com.pholser.junit.quickcheck.generator.*;
import com.pholser.junit.quickcheck.internal.Lists;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.math.BigDecimal.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.StreamSupport.*;

import static com.pholser.junit.quickcheck.internal.Lists.*;
import static com.pholser.junit.quickcheck.internal.Ranges.*;
import static com.pholser.junit.quickcheck.internal.Ranges.Type.*;
import static com.pholser.junit.quickcheck.internal.Reflection.*;
import static com.pholser.junit.quickcheck.internal.Sequences.*;

public class ArrayGenerator extends Generator<Object> {
    private final Class<?> componentType;
    private final Generator<?> component;

    private Size lengthRange;
    private boolean distinct;

    ArrayGenerator(Class<?> componentType, Generator<?> component) {
        super(Object.class);

        this.componentType = componentType;
        this.component = component;
    }

    /**
     * Tells this generator to produce values with a length within a specified
     * minimum and/or maximum, inclusive, chosen with uniform distribution.
     *
     * @param size annotation that gives the length constraints
     */
    public void configure(Size size) {
        this.lengthRange = size;
        checkRange(INTEGRAL, size.min(), size.max());
    }

    /**
     * Tells this generator to produce values which are distinct from each
     * other.
     *
     * @param distinct Generated values will be distinct if this param is not
     * null.
     */
    public void configure(Distinct distinct) {
        this.distinct = distinct != null;
    }

    @Override public Object generate(
        SourceOfRandomness random,
        GenerationStatus status) {

        int length = length(random, status);
        Object array = Array.newInstance(componentType, length);

        Stream<?> items =
            Stream.generate(() -> component.generate(random, status))
                .sequential();
        if (distinct)
            items = items.distinct();

        Iterator<?> iterator = items.iterator();
        for (int i = 0; i < length; ++i)
            Array.set(array, i, iterator.next());

        return array;
    }

    @Override public boolean canShrink(Object larger) {
        return larger.getClass().getComponentType() == componentType;
    }

    @Override public List<Object> doShrink(SourceOfRandomness random, Object larger) {
        int length = Array.getLength(larger);
        List<Object> asList = new ArrayList<>();
        for (int i = 0; i < length; ++i)
            asList.add(Array.get(larger, i));

        List<Object> shrinks = new ArrayList<>(removals(asList));

        @SuppressWarnings("unchecked")
        Stream<List<Object>> oneItemShrinks =
            shrinksOfOneItem(random, asList, (Shrink<Object>) component)
                .stream();
        if (distinct)
            oneItemShrinks = oneItemShrinks.filter(Lists::isDistinct);

        shrinks.addAll(
            oneItemShrinks
                .map(this::convert)
                .filter(this::inLengthRange)
                .collect(toList()));

        return shrinks;
    }

    @Override public void provide(Generators provided) {
        super.provide(provided);

        component.provide(provided);
    }

    @Override public BigDecimal magnitude(Object value) {
        int length = Array.getLength(value);
        if (length == 0)
            return ZERO;

        BigDecimal elementsMagnitude =
            IntStream.range(0, length)
                .mapToObj(i -> component.magnitude(Array.get(value, i)))
                .reduce(ZERO, BigDecimal::add);
        return BigDecimal.valueOf(length).multiply(elementsMagnitude);
    }

    @Override public void configure(AnnotatedType annotatedType) {
        super.configure(annotatedType);

        List<AnnotatedType> annotated = annotatedComponentTypes(annotatedType);
        if (!annotated.isEmpty())
            component.configure(annotated.get(0));
    }

    private int length(SourceOfRandomness random, GenerationStatus status) {
        return lengthRange != null
            ? random.nextInt(lengthRange.min(), lengthRange.max())
            : status.size();
    }

    private boolean inLengthRange(Object items) {
        int length = Array.getLength(items);
        return lengthRange == null
            || (length >= lengthRange.min() && length <= lengthRange.max());
    }

    private List<Object> removals(List<?> items) {
        return stream(halving(items.size()).spliterator(), false)
            .map(i -> removeFrom(items, i))
            .flatMap(Collection::stream)
            .map(this::convert)
            .filter(this::inLengthRange)
            .collect(toList());
    }

    private Object convert(List<?> items) {
        Object array = Array.newInstance(componentType, items.size());
        for (int i = 0; i < items.size(); ++i)
            Array.set(array, i, items.get(i));
        return array;
    }
}

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

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
 * <p>Base class for generators of {@link Map}s.</p>
 *
 * <p>The generated map has a number of entries limited by
 * {@link GenerationStatus#size()}, or else by the attributes of a {@link Size}
 * marking. The individual keys and values will have types corresponding to the
 * theory parameter's type arguments.</p>
 *
 * @param <T> the type of map generated
 */
public abstract class MapGenerator<T extends Map> extends ComponentizedGenerator<T> {
    private Size sizeRange;

    protected MapGenerator(Class<T> type) {
        super(type);
    }

    /**
     * <p>Tells this generator to add key-value pairs to the generated map a
     * number of times within a specified minimum and/or maximum, inclusive,
     * chosen with uniform distribution.</p>
     *
     * <p>Note that maps disallow duplicate keys, so the number of pairs added
     * may not be equal to the map's {@link Map#size()}.</p>
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
        for (int i = 0; i < size; ++i) {
            Object key = componentGenerators().get(0).generate(random, status);
            Object value = componentGenerators().get(1).generate(random, status);
            if (okToAdd(key, value))
                items.put(key, value);
        }

        return items;
    }

    @Override public List<T> doShrink(SourceOfRandomness random, T larger) {
        @SuppressWarnings("unchecked")
        List<Entry<?, ?>> entries = new ArrayList<>(larger.entrySet());

        List<T> shrinks = new ArrayList<>();
        shrinks.addAll(removals(entries));

        @SuppressWarnings("unchecked")
        Shrink<Entry<?, ?>> entryShrink = entryShrinker(
            (Shrink<Object>) componentGenerators().get(0),
            (Shrink<Object>) componentGenerators().get(1));

        List<List<Entry<?, ?>>> oneEntryShrinks = shrinksOfOneItem(random, entries, entryShrink);
        shrinks.addAll(oneEntryShrinks.stream()
            .map(this::convert)
            .filter(this::inSizeRange)
            .collect(Collectors.toList()));

        return shrinks;
    }

    @Override public int numberOfNeededComponents() {
        return 2;
    }

    protected final T empty() {
        return instantiate(findConstructor(types().get(0)));
    }

    protected boolean okToAdd(Object key, Object value) {
        return true;
    }

    private boolean inSizeRange(T target) {
        return sizeRange == null
            || (target.size() >= sizeRange.min() && target.size() <= sizeRange.max());
    }

    private int size(SourceOfRandomness random, GenerationStatus status) {
        return sizeRange != null
            ? random.nextInt(sizeRange.min(), sizeRange.max())
            : status.size();
    }

    private List<T> removals(List<Entry<?, ?>> items) {
        return stream(halving(items.size()).spliterator(), false)
            .map(i -> removeFrom(items, i))
            .flatMap(Collection::stream)
            .map(this::convert)
            .filter(this::inSizeRange)
            .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private T convert(List<?> entries) {
        T converted = empty();

        for (Object each : entries) {
            Entry<?, ?> entry = (Entry<?, ?>) each;
            converted.put(entry.getKey(), entry.getValue());
        }

        return converted;
    }

    private Shrink<Entry<?, ?>> entryShrinker(
        Shrink<Object> keyShrinker,
        Shrink<Object> valueShrinker) {

        return (r, e) -> {
            @SuppressWarnings("unchecked")
            Entry<Object, Object> entry = (Entry<Object, Object>) e;

            List<Object> keyShrinks = keyShrinker.shrink(r, entry.getKey());
            List<Object> valueShrinks = valueShrinker.shrink(r, entry.getValue());
            List<Entry<?, ?>> shrinks = new ArrayList<>();
            shrinks.addAll(IntStream.range(0, keyShrinks.size())
                .mapToObj(i -> new SimpleEntry<>(keyShrinks.get(i), entry.getValue()))
                .collect(Collectors.toList()));
            shrinks.addAll(IntStream.range(0, valueShrinks.size())
                .mapToObj(i -> new SimpleEntry<>(entry.getKey(), valueShrinks.get(i)))
                .collect(Collectors.toList()));
            return shrinks;
        };
    }
}

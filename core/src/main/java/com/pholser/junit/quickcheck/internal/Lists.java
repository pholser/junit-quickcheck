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

package com.pholser.junit.quickcheck.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.pholser.junit.quickcheck.generator.Shrink;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.util.Collections.*;

public final class Lists {
    private Lists() {
        throw new UnsupportedOperationException();
    }

    public static <T> List<List<T>> removeFrom(List<T> target, int howMany) {
        if (howMany < 0)
            throw new IllegalArgumentException("Can't remove " + howMany + " elements from a list");
        if (howMany == 0)
            return singletonList(target);
        if (howMany > target.size())
            return emptyList();

        List<T> left = target.subList(0, howMany);
        List<T> right = target.subList(howMany, target.size());
        if (right.isEmpty())
            return singletonList(emptyList());

        List<List<T>> removals = new ArrayList<>();
        removals.add(right);
        removals.addAll(removeFrom(right, howMany)
            .stream()
            .map(r -> {
                List<T> items = new ArrayList<>(left);
                items.addAll(r);
                return items;
            })
            .collect(Collectors.toList()));
        return removals;
    }

    public static <T> List<List<T>> shrinksOfOneItem(
        SourceOfRandomness random,
        List<T> target,
        Shrink<T> shrink) {

        if (target.isEmpty())
            return new ArrayList<>();

        T head = target.get(0);
        List<T> tail = target.subList(1, target.size());

        List<List<T>> shrinks = new ArrayList<>();
        shrinks.addAll(shrink.shrink(random, head)
            .stream()
            .map(i -> {
                List<T> items = new ArrayList<>();
                items.add(i);
                items.addAll(tail);
                return items;
            })
            .collect(Collectors.toList()));
        shrinks.addAll(shrinksOfOneItem(random, tail, shrink)
            .stream()
            .map(s -> {
                List<T> items = new ArrayList<>();
                items.add(head);
                items.addAll(s);
                return items;
            })
            .collect(Collectors.toList()));

        return shrinks;
    }
}

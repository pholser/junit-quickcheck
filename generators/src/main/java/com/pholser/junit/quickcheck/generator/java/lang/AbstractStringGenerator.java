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

package com.pholser.junit.quickcheck.generator.java.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.Lists;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.util.stream.StreamSupport.*;

import static com.pholser.junit.quickcheck.internal.Lists.*;
import static com.pholser.junit.quickcheck.internal.Sequences.*;

/**
 * <p>Base class for generators of values of type {@link String}.</p>
 *
 * <p>The generated values will have {@linkplain String#length()} decided by
 * {@link GenerationStatus#size()}.</p>
 */
public abstract class AbstractStringGenerator extends Generator<String> {
    protected AbstractStringGenerator() {
        super(String.class);
    }

    @Override public String generate(SourceOfRandomness random, GenerationStatus status) {
        int[] codePoints = new int[status.size()];

        for (int i = 0; i < codePoints.length; ++i)
            codePoints[i] = nextCodePoint(random);

        return new String(codePoints, 0, codePoints.length);
    }

    @Override public boolean canShrink(Object larger) {
        return super.canShrink(larger) && codePointsInRange((String) larger);
    }

    @Override public List<String> doShrink(SourceOfRandomness random, String larger) {
        List<String> shrinks = new ArrayList<>();

        List<Integer> codePoints = larger.codePoints().boxed().collect(Collectors.toList());
        shrinks.addAll(removals(codePoints));

        List<List<Integer>> oneItemShrinks =
            shrinksOfOneItem(random, codePoints, new CodePointShrink(this::codePointInRange));
        shrinks.addAll(oneItemShrinks.stream()
            .map(this::convert)
            .filter(this::codePointsInRange)
            .collect(Collectors.toList()));

        return shrinks;
    }

    protected abstract int nextCodePoint(SourceOfRandomness random);

    protected abstract boolean codePointInRange(int codePoint);

    private boolean codePointsInRange(String s) {
        return s.codePoints().allMatch(this::codePointInRange);
    }

    private List<String> removals(List<Integer> codePoints) {
        return stream(halving(codePoints.size()).spliterator(), false)
            .map(i -> Lists.removeFrom(codePoints, i))
            .flatMap(Collection::stream)
            .map(this::convert)
            .collect(Collectors.toList());
    }

    private String convert(List<Integer> codePoints) {
        StringBuilder s = new StringBuilder();
        codePoints.forEach(s::appendCodePoint);
        return s.toString();
    }
}

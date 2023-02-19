/*
 The MIT License

 Copyright (c) 2010-2021 Paul R. Holser, Jr.

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

import static com.pholser.junit.quickcheck.internal.Lists.shrinksOfOneItem;
import static com.pholser.junit.quickcheck.internal.Ranges.Type.INTEGRAL;
import static com.pholser.junit.quickcheck.internal.Ranges.checkRange;
import static com.pholser.junit.quickcheck.internal.Sequences.halving;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.internal.Lists;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>Base class for generators of values of type {@link String}.</p>
 *
 * <p>The generated values will have {@linkplain String#length()} decided by
 * {@link GenerationStatus#size()}.</p>
 */
public abstract class AbstractStringGenerator extends Generator<String> {
    private Size sizeRange;

    protected AbstractStringGenerator() {
        super(String.class);
    }

    public void configure(Size size) {
        this.sizeRange = size;
        checkRange(INTEGRAL, size.min(), size.max());
    }

    @Override public String generate(
        SourceOfRandomness random,
        GenerationStatus status) {

        int[] codePoints = new int[size(random, status)];

        for (int i = 0; i < codePoints.length; ++i)
            codePoints[i] = nextCodePoint(random);

        return new String(codePoints, 0, codePoints.length);
    }

    @Override public boolean canShrink(Object larger) {
        return super.canShrink(larger) && codePointsInRange((String) larger);
    }

    @Override public List<String> doShrink(
        SourceOfRandomness random,
        String larger) {

        List<Integer> codePoints =
            larger.codePoints().boxed().collect(toList());
        List<String> shrinks = new ArrayList<>(removals(codePoints));

        List<List<Integer>> oneItemShrinks =
            shrinksOfOneItem(
                random,
                codePoints,
                new CodePointShrink(this::codePointInRange));
        shrinks.addAll(
            oneItemShrinks.stream()
                .map(this::convert)
                .filter(this::inSizeRange)
                .filter(this::codePointsInRange)
                .collect(toList()));

        return shrinks.stream().filter(this::inSizeRange).collect(toList());
    }

    @Override public BigDecimal magnitude(Object value) {
        return BigDecimal.valueOf(narrow(value).length());
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
            .collect(toList());
    }

    private String convert(List<Integer> codePoints) {
        StringBuilder s = new StringBuilder();
        codePoints.forEach(s::appendCodePoint);
        return s.toString();
    }

    private int size(SourceOfRandomness random, GenerationStatus status) {
        return sizeRange != null
            ? random.nextInt(sizeRange.min(), sizeRange.max())
            : status.size();
    }

    private boolean inSizeRange(String s) {
        System.out.println(sizeRange);
        System.out.println(s.length());

        return sizeRange == null
            || (s.length() >= sizeRange.min()
                && s.length() <= sizeRange.max());
    }
}

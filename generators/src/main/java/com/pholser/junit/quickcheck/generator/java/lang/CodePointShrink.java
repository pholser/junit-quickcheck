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
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.pholser.junit.quickcheck.generator.Shrink;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.lang.Character.*;
import static java.util.Collections.*;
import static java.util.Comparator.*;

class CodePointShrink implements Shrink<Integer> {
    private final Predicate<? super Integer> filter;

    CodePointShrink(Predicate<? super Integer> filter) {
        this.filter = filter;
    }

    @Override public List<Integer> shrink(SourceOfRandomness random, Object larger) {
        int codePoint = (Integer) larger;

        List<Integer> shrinks = new ArrayList<>();
        addAll(shrinks, (int) 'a', (int) 'b', (int) 'c');
        if (isUpperCase(codePoint))
            shrinks.add(Character.toLowerCase(codePoint));
        addAll(shrinks, (int) 'A', (int) 'B', (int) 'C',
            (int) '1', (int) '2', (int) '3',
            (int) ' ', (int) '\n');
        reverse(shrinks);

        Comparator<Integer> comparator =
            comparing((Function<Integer, Boolean>) Character::isLowerCase)
                .thenComparing((Function<Integer, Boolean>) Character::isUpperCase)
                .thenComparing((Function<Integer, Boolean>) Character::isDigit)
                .thenComparing(cp -> Integer.valueOf(' ').equals(cp))
                .thenComparing((Function<Integer, Boolean>) Character::isSpaceChar)
                .thenComparing(naturalOrder());
        return shrinks.stream()
                .filter(filter)
                .filter(cp -> comparator.compare(cp, codePoint) < 0)
                .distinct()
                .collect(Collectors.toList());
    }
}

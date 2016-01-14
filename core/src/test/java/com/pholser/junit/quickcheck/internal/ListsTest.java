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

import java.util.List;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static com.google.common.collect.Lists.*;
import static com.google.common.collect.Sets.*;
import static java.util.Collections.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.junit.rules.ExpectedException.*;

public class ListsTest {
    @Rule public final ExpectedException thrown = none();
    @Rule public final MockitoRule mockito = MockitoJUnit.rule();

    @Mock private SourceOfRandomness random;

    @Test public void rejectsNegativeRemovalCount() {
        thrown.expect(IllegalArgumentException.class);

        Lists.removeFrom(newArrayList("abc"), -1);
    }

    @Test public void removalsOfZeroElementsFromAList() {
        List<Integer> target = newArrayList(1, 2, 3);

        assertEquals(singletonList(target), Lists.removeFrom(target, 0));
    }

    @Test public void removalsFromAnEmptyList() {
        assertEquals(emptyList(), Lists.removeFrom(emptyList(), 1));
    }

    @Test public void singleRemovalsFromASingletonList() {
        assertEquals(singletonList(emptyList()), Lists.removeFrom(singletonList('a'), 1));
    }

    @Test public void singleRemovalsFromADoubletonList() {
        assertEquals(
            newHashSet(singletonList('a'), singletonList('b')),
            newHashSet(Lists.removeFrom(newArrayList('a', 'b'), 1)));
    }

    @Test public void doubleRemovalsFromADoubletonList() {
        assertEquals(singletonList(emptyList()), Lists.removeFrom(newArrayList('a', 'b'), 2));
    }

    @Test public void tooManyRemovalsFromADoubletonList() {
        assertEquals(emptyList(), Lists.removeFrom(newArrayList('a', 'b'), 3));
    }

    @Test public void shrinksOfEmptyList() {
        assertEquals(emptyList(), Lists.shrinksOfOneItem(random, emptyList(), null));
    }

    @Test public void shrinksOfNonEmptyList() {
        List<List<Integer>> shrinks = Lists.shrinksOfOneItem(
            random,
            newArrayList(1, 2, 3),
            (r, i) -> {
                assumeThat(r, sameInstance(random));
                return newArrayList(4, 5);
            });

        assertEquals(
            newArrayList(
                newArrayList(4, 2, 3),
                newArrayList(5, 2, 3),
                newArrayList(1, 4, 3),
                newArrayList(1, 5, 3),
                newArrayList(1, 2, 4),
                newArrayList(1, 2, 5)
            ),
            shrinks);
    }
}

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

import org.junit.Test;

import static com.google.common.collect.Lists.*;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.junit.Assert.*;

public class CartesianIteratorTest {
    @Test public void noSources() {
        CartesianIterator<Integer> iter = new CartesianIterator<>(emptyList());

        List<List<Integer>> asList = newArrayList(iter);

        assertEquals(0, asList.size());
    }

    @Test public void singleSourceIsEmpty() {
        CartesianIterator<Integer> iter =
            new CartesianIterator<>(
                singletonList(
                    new ArrayList<Integer>().iterator()
                )
            );

        List<List<Integer>> result = newArrayList(iter);

        assertEquals(0, result.size());
    }

    @Test public void multipleSourcesAreEmpty() {
        CartesianIterator<Integer> iter =
            new CartesianIterator<>(
                asList(
                    new ArrayList<Integer>().iterator(),
                    new ArrayList<Integer>().iterator(),
                    new ArrayList<Integer>().iterator()
                )
            );

        List<List<Integer>> result = newArrayList(iter);

        assertEquals(0, result.size());
    }

    @Test public void onlyOneSourceIsEmpty() {
        CartesianIterator<Integer> iter =
            new CartesianIterator<>(
                asList(
                    asList(1, 2, 3).iterator(),
                    new ArrayList<Integer>().iterator(),
                    asList(4, 5, 6, 7).iterator()
                )
            );

        List<List<Integer>> result = newArrayList(iter);

        assertEquals(0, result.size());
    }

    @Test public void oneSourceOfOne() {
        CartesianIterator<Integer> iter =
            new CartesianIterator<>(
                singletonList(
                    singletonList(1).iterator()
                )
            );

        List<List<Integer>> result = newArrayList(iter);

        assertEquals(singletonList(singletonList(1)), result);
    }

    @Test public void manySourcesOfOne() {
        CartesianIterator<Integer> iter =
            new CartesianIterator<>(
                asList(
                    singletonList(1).iterator(),
                    singletonList(2).iterator(),
                    singletonList(3).iterator(),
                    singletonList(4).iterator()
                )
            );

        List<List<Integer>> result = newArrayList(iter);

        assertEquals(singletonList(asList(1, 2, 3, 4)), result);
    }

    @Test public void oneSourceOfManyAtPositionZero() {
        CartesianIterator<Integer> iter =
            new CartesianIterator<>(
                asList(
                    asList(1, 2, 3).iterator(),
                    singletonList(4).iterator(),
                    singletonList(5).iterator(),
                    singletonList(6).iterator()
                )
            );

        List<List<Integer>> result = newArrayList(iter);

        assertEquals(
            asList(
                asList(1, 4, 5, 6),
                asList(2, 4, 5, 6),
                asList(3, 4, 5, 6)
            ),
            result);
    }

    @Test public void oneSourceOfManyAtPositionMid() {
        CartesianIterator<Integer> iter =
            new CartesianIterator<>(
                asList(
                    singletonList(1).iterator(),
                    singletonList(2).iterator(),
                    asList(3, 4, 5).iterator(),
                    singletonList(6).iterator()
                )
            );

        List<List<Integer>> result = newArrayList(iter);

        assertEquals(
            asList(
                asList(1, 2, 3, 6),
                asList(1, 2, 4, 6),
                asList(1, 2, 5, 6)
            ),
            result);
    }

    @Test public void oneSourceOfManyAtPositionLast() {
        CartesianIterator<Integer> iter =
            new CartesianIterator<>(
                asList(
                    singletonList(1).iterator(),
                    singletonList(2).iterator(),
                    singletonList(3).iterator(),
                    asList(4, 5, 6).iterator()
                )
            );

        List<List<Integer>> result = newArrayList(iter);

        assertEquals(
            asList(
                asList(1, 2, 3, 4),
                asList(1, 2, 3, 5),
                asList(1, 2, 3, 6)
            ),
            result);
    }

    @Test public void manySourcesOfMany() {
        CartesianIterator<Integer> iter =
            new CartesianIterator<>(
                asList(
                    asList(0, 1, 2, 3, 4).iterator(),
                    asList(5, 6).iterator(),
                    asList(7, 8, 9).iterator(),
                    asList(10, 11, 12).iterator()
                )
            );

        List<List<Integer>> result = newArrayList(iter);

        assertEquals(
            asList(
                asList(0, 5, 7, 10), asList(1, 5, 7, 10), asList(2, 5, 7, 10), asList(3, 5, 7, 10), asList(4, 5, 7, 10),
                asList(0, 6, 7, 10), asList(1, 6, 7, 10), asList(2, 6, 7, 10), asList(3, 6, 7, 10), asList(4, 6, 7, 10),
                asList(0, 5, 8, 10), asList(1, 5, 8, 10), asList(2, 5, 8, 10), asList(3, 5, 8, 10), asList(4, 5, 8, 10),
                asList(0, 6, 8, 10), asList(1, 6, 8, 10), asList(2, 6, 8, 10), asList(3, 6, 8, 10), asList(4, 6, 8, 10),
                asList(0, 5, 9, 10), asList(1, 5, 9, 10), asList(2, 5, 9, 10), asList(3, 5, 9, 10), asList(4, 5, 9, 10),
                asList(0, 6, 9, 10), asList(1, 6, 9, 10), asList(2, 6, 9, 10), asList(3, 6, 9, 10), asList(4, 6, 9, 10),
                asList(0, 5, 7, 11), asList(1, 5, 7, 11), asList(2, 5, 7, 11), asList(3, 5, 7, 11), asList(4, 5, 7, 11),
                asList(0, 6, 7, 11), asList(1, 6, 7, 11), asList(2, 6, 7, 11), asList(3, 6, 7, 11), asList(4, 6, 7, 11),
                asList(0, 5, 8, 11), asList(1, 5, 8, 11), asList(2, 5, 8, 11), asList(3, 5, 8, 11), asList(4, 5, 8, 11),
                asList(0, 6, 8, 11), asList(1, 6, 8, 11), asList(2, 6, 8, 11), asList(3, 6, 8, 11), asList(4, 6, 8, 11),
                asList(0, 5, 9, 11), asList(1, 5, 9, 11), asList(2, 5, 9, 11), asList(3, 5, 9, 11), asList(4, 5, 9, 11),
                asList(0, 6, 9, 11), asList(1, 6, 9, 11), asList(2, 6, 9, 11), asList(3, 6, 9, 11), asList(4, 6, 9, 11),
                asList(0, 5, 7, 12), asList(1, 5, 7, 12), asList(2, 5, 7, 12), asList(3, 5, 7, 12), asList(4, 5, 7, 12),
                asList(0, 6, 7, 12), asList(1, 6, 7, 12), asList(2, 6, 7, 12), asList(3, 6, 7, 12), asList(4, 6, 7, 12),
                asList(0, 5, 8, 12), asList(1, 5, 8, 12), asList(2, 5, 8, 12), asList(3, 5, 8, 12), asList(4, 5, 8, 12),
                asList(0, 6, 8, 12), asList(1, 6, 8, 12), asList(2, 6, 8, 12), asList(3, 6, 8, 12), asList(4, 6, 8, 12),
                asList(0, 5, 9, 12), asList(1, 5, 9, 12), asList(2, 5, 9, 12), asList(3, 5, 9, 12), asList(4, 5, 9, 12),
                asList(0, 6, 9, 12), asList(1, 6, 9, 12), asList(2, 6, 9, 12), asList(3, 6, 9, 12), asList(4, 6, 9, 12)
            ),
            result);
    }

    @Test public void manySourcesOfEqualSize() {
        CartesianIterator<Integer> iter =
            new CartesianIterator<>(
                asList(
                    asList(0, 1, 2).iterator(),
                    asList(0, 1, 2).iterator(),
                    asList(0, 1, 2).iterator()
                )
            );

        List<List<Integer>> result = newArrayList(iter);

        assertEquals(
            asList(
                asList(0, 0, 0), asList(1, 0, 0), asList(2, 0, 0),
                asList(0, 1, 0), asList(1, 1, 0), asList(2, 1, 0),
                asList(0, 2, 0), asList(1, 2, 0), asList(2, 2, 0),
                asList(0, 0, 1), asList(1, 0, 1), asList(2, 0, 1),
                asList(0, 1, 1), asList(1, 1, 1), asList(2, 1, 1),
                asList(0, 2, 1), asList(1, 2, 1), asList(2, 2, 1),
                asList(0, 0, 2), asList(1, 0, 2), asList(2, 0, 2),
                asList(0, 1, 2), asList(1, 1, 2), asList(2, 1, 2),
                asList(0, 2, 2), asList(1, 2, 2), asList(2, 2, 2)
            ),
            result);
    }
}

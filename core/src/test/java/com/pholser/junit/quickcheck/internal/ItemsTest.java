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

import java.util.LinkedHashSet;
import java.util.Set;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ItemsTest {
    @Rule public final MockitoRule mockito = MockitoJUnit.rule();

    @Mock private SourceOfRandomness random;
    private Weighted<String> first;
    private Weighted<String> second;
    private Weighted<String> third;

    @Before public void setUp() {
        first = new Weighted<>("a", 2);
        second = new Weighted<>("b", 3);
        third = new Weighted<>("c", 4);
    }

    @Test public void choosingFromSet() {
        Set<String> names = new LinkedHashSet<>(asList("alpha", "bravo", "charlie", "delta"));
        when(random.nextInt(names.size())).thenReturn(2);

        assertEquals("charlie", Items.choose(names, random));
    }

    @Test public void singleWeightedItem() {
        when(random.nextInt(2)).thenReturn(1);

        assertEquals("a", Items.chooseWeighted(singletonList(first), random));
    }

    @Test public void choosingFirstOfManyWeightedItems() {
        when(random.nextInt(9)).thenReturn(0).thenReturn(1);

        assertEquals("a", Items.chooseWeighted(asList(first, second, third), random));
        assertEquals("a", Items.chooseWeighted(asList(first, second, third), random));
    }

    @Test public void choosingMiddleOfManyWeightedItems() {
        when(random.nextInt(9)).thenReturn(2).thenReturn(3).thenReturn(4);

        assertEquals("b", Items.chooseWeighted(asList(first, second, third), random));
        assertEquals("b", Items.chooseWeighted(asList(first, second, third), random));
        assertEquals("b", Items.chooseWeighted(asList(first, second, third), random));
    }

    @Test public void choosingLastOfManyWeightedItems() {
        when(random.nextInt(9)).thenReturn(5).thenReturn(6).thenReturn(7).thenReturn(8);

        assertEquals("c", Items.chooseWeighted(asList(first, second, third), random));
        assertEquals("c", Items.chooseWeighted(asList(first, second, third), random));
        assertEquals("c", Items.chooseWeighted(asList(first, second, third), random));
        assertEquals("c", Items.chooseWeighted(asList(first, second, third), random));
    }

    @Test public void choosingFromEmptyCollection() {
        try {
            Items.chooseWeighted(emptyList(), random);
        } catch (AssertionError expected) {
            assertEquals("sample = 0, range = 0", expected.getMessage());
        }
    }
}

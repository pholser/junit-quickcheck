/*
 The MIT License

 Copyright (c) 2010-2017 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck;

import java.util.Optional;

import com.pholser.junit.quickcheck.generator.Gen;
import org.junit.Test;

import static org.junit.Assert.*;

public class FilterTest {
    @Test public void filter() {
        int[] i = { 0 };
        Gen<Integer> ints = (random, status) -> i[0]++;
        Gen<Integer> bigger = ints.filter(n -> n >= 10);

        int result = bigger.generate(null, null);

        assertEquals(10, result);
        assertEquals(11, i[0]);
    }

    @Test public void filterOptional() {
        int[] i = { 0 };
        Gen<Integer> ints = (random, status) -> i[0]++;
        Gen<Optional<Integer>> smaller = ints.filterOptional(n -> n < 2);

        int first = smaller.generate(null, null)
            .orElseThrow(AssertionError::new);
        assertEquals(0, first);

        int second = smaller.generate(null, null)
            .orElseThrow(AssertionError::new);
        assertEquals(1, second);

        assertEquals(Optional.empty(), smaller.generate(null, null));
        assertEquals(3, i[0]);
    }
}

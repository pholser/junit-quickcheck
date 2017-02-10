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

import com.pholser.junit.quickcheck.generator.Gen;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapFlatMapTest {
    @Test public void map() {
        Gen<Integer> five = (random, status) -> 5;

        Gen<String> mapped = five.map(String::valueOf);

        assertEquals("5", mapped.generate(null, null));
    }

    @Test public void flatMap() {
        class Coordinate {
            final int x;
            final int y;

            private Coordinate(int x, int y) {
                this.x = x;
                this.y = y;
            }
        }

        Gen<Integer> five = (random, status) -> 5;
        Gen<Integer> six = (random, status) -> 6;
        Gen<Coordinate> flatMapped =
            five.flatMap(i ->
                six.map(j ->
                    new Coordinate(i, j)));

        Coordinate generated = flatMapped.generate(null, null);

        assertEquals(5, generated.x);
        assertEquals(6, generated.y);
    }
}

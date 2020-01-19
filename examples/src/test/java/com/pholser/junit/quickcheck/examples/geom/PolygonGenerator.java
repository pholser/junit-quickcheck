/*
 The MIT License

 Copyright (c) 2010-2020 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.examples.geom;

import java.util.List;

import com.pholser.junit.quickcheck.examples.geom.Point;
import com.pholser.junit.quickcheck.examples.geom.Polygon;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public class PolygonGenerator extends Generator<Polygon> {
    private static final @Size(min = 3, max = 30) List<Point> POINTS = null;

    public PolygonGenerator() {
        super(Polygon.class);
    }

    @Override public Polygon generate(
        SourceOfRandomness r,
        GenerationStatus s) {

        Generator<List<Point>> points =
            (Generator<List<Point>>) gen().field(
                getClass(),
                "POINTS");

        return new Polygon(points.generate(r, s));
    }

    @Override public List<Polygon> doShrink(
        SourceOfRandomness r,
        Polygon larger) {

        Generator<Point> pointGen = gen().type(Point.class);

        return singletonList(
            new Polygon(
                larger.points.stream()
                    .map(p -> pointGen.shrink(r, p).get(0))
                    .collect(toList())));
    }
}

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

package com.pholser.junit.quickcheck.examples.geom;

import static com.google.common.collect.Streams.zip;
import static java.util.stream.Collectors.toList;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import java.util.List;

public class SegmentGenerator extends Generator<Segment> {
    public SegmentGenerator() {
        super(Segment.class);
    }

    @Override public Segment generate(
        SourceOfRandomness r,
        GenerationStatus s) {

        Generator<Point> pointGen = gen().type(Point.class);
        return new Segment(
            pointGen.generate(r, s),
            pointGen.generate(r, s));
    }

    @Override public List<Segment> doShrink(
        SourceOfRandomness r,
        Segment larger) {

        Generator<Point> pointGen = gen().type(Point.class);

        return zip(
            pointGen.shrink(r, larger.a).stream(),
            pointGen.shrink(r, larger.b).stream(),
            Segment::new
        ).collect(toList());
    }
}

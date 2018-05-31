/*
 The MIT License

 Copyright (c) 2010-2018 Paul R. Holser, Jr.

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

import static com.pholser.junit.quickcheck.examples.geom.Point.Orientation.*;
import static java.lang.Math.*;

/**
 * @see <a href="https://www.geeksforgeeks.org/program-check-three-points-collinear">Article</a>
 * @see <a href="https://www.geeksforgeeks.org/how-to-check-if-a-given-point-lies-inside-a-polygon">Article</a>
 */
public final class Point {
    enum Orientation {
        COLLINEAR,
        CLOCKWISE,
        COUNTERCLOCKWISE
    }

    final double x;
    final double y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    boolean collinearWith(Point a, Point b) {
        return COLLINEAR == orientation(a, this, b);
    }

    boolean between(Point a, Point b) {
        return collinearWith(a, b)
            && x <= max(a.x, b.x) && x >= min(a.x, b.x)
            && y <= max(a.y, b.y) && y >= min(a.y, b.y);
    }

    static Orientation orientation(Point p, Point q, Point r) {
        double val =
            (q.y - p.y) * (r.x - q.x)
                - (q.x - p.x) * (r.y - q.y);

        if (val == 0)
            return COLLINEAR;
        return val > 0 ? CLOCKWISE : COUNTERCLOCKWISE;
    }

    @Override public String toString() {
        return String.format("(%f, %f)", x, y);
    }
}

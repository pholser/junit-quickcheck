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

import java.util.ArrayList;
import java.util.List;

import static com.pholser.junit.quickcheck.examples.geom.Point.Orientation.*;
import static com.pholser.junit.quickcheck.examples.geom.Point.*;
import static java.util.Comparator.*;

/**
 * @see <a href="https://www.geeksforgeeks.org/how-to-check-if-a-given-point-lies-inside-a-polygon/">Article</a>
 * @see <a href="https://bit.ly/2xm22oj">Stack Overflow</a>
 */
public final class Polygon {
    final List<Point> points;

    public Polygon(List<Point> points) {
        if (points.size() < 3) {
            throw new IllegalArgumentException(
                "Need at least three points, got " + points.size());
        }

        this.points = new ArrayList<>(points);
        this.points.sort(comparingDouble(a -> Math.atan2(a.y, a.x)));
    }

    boolean convex() {
        if (points.size() < 4)
            return true;

        boolean sign = false;
        int n = points.size();

        for (int i = 0; i < n; i++) {
            double dx1 =
                points.get((i + 2) % n).x - points.get((i + 1) % n).x;
            double dy1 =
                points.get((i + 2) % n).y - points.get((i + 1) % n).y;
            double dx2 =
                points.get(i).x - points.get((i + 1) % n).x;
            double dy2 =
                points.get(i).y - points.get((i + 1) % n).y;
            double zCrossProduct = dx1 * dy2 - dy1 * dx2;

            if (i == 0)
                sign = zCrossProduct > 0;
            else if (sign != (zCrossProduct > 0))
                return false;
        }

        return true;
    }

    boolean contains(Point p) {
        Point extreme = new Point(Integer.MAX_VALUE, p.y);
        Segment pToExtreme = new Segment(p, extreme);

        // Count intersections of the above line with sides of polygon
        int count = 0;
        int i = 0;

        do {
            int next = (i + 1) % points.size();

            Segment iToNext = new Segment(points.get(i), points.get(next));
            if (iToNext.intersects(pToExtreme)) {
                if (orientation(points.get(i), p, points.get(next)) == COLLINEAR)
                    return p.between(points.get(i), points.get(next));

                ++count;
            }

            i = next;
        } while (i != 0);

        return count % 2 != 0;
    }

    @Override public String toString() {
        return points.toString();
    }
}

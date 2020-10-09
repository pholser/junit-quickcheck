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

package com.pholser.junit.quickcheck.examples.tree;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.examples.tree.visitor.TreeDeepestLeafVisitor;
import com.pholser.junit.quickcheck.examples.tree.visitor.TreeDepthVisitor;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import java.util.AbstractMap.SimpleImmutableEntry;
import org.junit.runner.RunWith;

@RunWith(JUnitQuickcheck.class)
public class TreePropertyTest {
    @Property public void deepestLeafConstrained(
        @From(TreeMaker.class)
        @Depth(min = 10, max = 10)
        Tree t) {

        TreeDeepestLeafVisitor visitor = new TreeDeepestLeafVisitor();

        @SuppressWarnings("unchecked")
        SimpleImmutableEntry<String, Integer> result =
            (SimpleImmutableEntry<String, Integer>) t.accept(visitor);

        assertThat(result.getValue(), lessThanOrEqualTo(10));
    }

    @Property public void depthConstrained(
        @From(TreeMaker.class)
        @Depth(min = 10, max = 10)
        Tree t) {

        TreeDepthVisitor visitor = new TreeDepthVisitor();

        int result = (int) t.accept(visitor);

        assertThat(result, lessThanOrEqualTo(10));
    }
}

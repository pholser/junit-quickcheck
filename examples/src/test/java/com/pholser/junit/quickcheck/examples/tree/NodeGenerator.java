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

package com.pholser.junit.quickcheck.examples.tree;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class NodeGenerator extends Generator<Node> {
    public NodeGenerator() {
        super(Node.class);
    }

    @Override public Node generate(SourceOfRandomness r, GenerationStatus s) {
        int depth = s.valueOf(TreeKeys.DEPTH).orElse(1);

        if (depth == 1) {
            Generator<Tree> leafOrEmpty = gen().oneOf(Leaf.class, Empty.class);
            return new Node(
                leafOrEmpty.generate(r, s),
                leafOrEmpty.generate(r, s)
            );
        }

        Generator<Tree> subtree =
            gen().oneOf(Node.class, Leaf.class, Empty.class);
        s.setValue(TreeKeys.DEPTH, depth - 1);
        return new Node(
            subtree.generate(r, s),
            subtree.generate(r, s)
        );
    }

    @Override public boolean canRegisterAsType(Class<?> type) {
        return Tree.class.isAssignableFrom(type);
    }
}

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

package com.pholser.junit.quickcheck.examples.tree;

import static com.pholser.junit.quickcheck.internal.Ranges.checkRange;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.Ranges;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class TreeMaker extends Generator<Tree> {
    private Depth range;

    public TreeMaker() {
        super(Tree.class);
    }

    public void configure(Depth range) {
        if (range != null)
            checkRange(Ranges.Type.INTEGRAL, range.min(), range.max());

        this.range = range;
    }

    @Override public Tree generate(SourceOfRandomness r, GenerationStatus s) {
        int depth = this.range == null
            ? s.size() / 2
            : r.nextInt(this.range.min(), this.range.max());

        switch (depth) {
            case 0:
                return gen().type(Empty.class).generate(r, s);
            case 1:
                return gen().type(Leaf.class).generate(r, s);
            default:
                s.setValue(TreeKeys.DEPTH, depth);
                return gen().type(Node.class).generate(r, s);
        }
    }

    @Override public boolean canRegisterAsType(Class<?> type) {
        return false;
    }
}

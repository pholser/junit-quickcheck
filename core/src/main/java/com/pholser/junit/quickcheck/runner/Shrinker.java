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

package com.pholser.junit.quickcheck.runner;

import java.util.List;
import java.util.Stack;

import com.pholser.junit.quickcheck.MinimalCounterexampleHook;
import com.pholser.junit.quickcheck.internal.generator.PropertyParameterGenerationContext;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

class Shrinker {
    private final FrameworkMethod method;
    private final TestClass testClass;
    private final AssertionError failure;
    private final int maxShrinks;
    private final int maxShrinkDepth;
    private final int maxShrinkTime;
    private final MinimalCounterexampleHook onMinimalCounterexample;

    private int shrinkAttempts;
    private long shrinkTimeout;

    Shrinker(
        FrameworkMethod method,
        TestClass testClass,
        AssertionError failure,
        int maxShrinks,
        int maxShrinkDepth,
        int maxShrinkTime,
        MinimalCounterexampleHook onMinimalCounterexample) {

        this.method = method;
        this.testClass = testClass;
        this.failure = failure;
        this.maxShrinks = maxShrinks;
        this.maxShrinkDepth = maxShrinkDepth;
        this.maxShrinkTime = maxShrinkTime;
        this.onMinimalCounterexample = onMinimalCounterexample;
    }

    void shrink(
        List<PropertyParameterGenerationContext> params,
        Object[] args,
        long[] initialSeeds)
        throws Throwable {

        Stack<ShrinkNode> nodes = new Stack<>();
        ShrinkNode counterexample =
            ShrinkNode.root(method, testClass, params, args, initialSeeds);
        counterexample.shrinks().forEach(nodes::push);

        shrinkTimeout = System.currentTimeMillis() + maxShrinkTime;

        while (shouldContinueShrinking(nodes)) {
            ShrinkNode next = nodes.pop();
            if (next.mightBePast(counterexample)) {
                boolean result = next.verifyProperty();
                ++shrinkAttempts;

                if (!result) {
                    counterexample = next;

                    List<ShrinkNode> shrinks = next.shrinks();
                    if (shrinks.isEmpty())
                        counterexample = counterexample.advanceToNextArg();
                    else
                        shrinks.forEach(nodes::push);
                }

                if (nodes.empty()) {
                    counterexample = counterexample.advanceToNextArg();
                    counterexample.shrinks().forEach(nodes::push);
                }
            }
        }

        handleMinimalCounterexample(counterexample);
        throw counterexample.fail(failure);
    }

    private void handleMinimalCounterexample(ShrinkNode counterexample) {
        Runnable repeat = () -> {
            try {
                counterexample.verifyProperty();
            } catch (Throwable e) {}
        };

        onMinimalCounterexample.handle(counterexample.getArgs(), repeat);
    }

    private boolean shouldContinueShrinking(Stack<ShrinkNode> nodes) {
        return shrinkAttempts < maxShrinks
            && shrinkTimeout >= System.currentTimeMillis()
            && !nodes.empty()
            && !nodes.peek().deeperThan(maxShrinkDepth);
    }
}

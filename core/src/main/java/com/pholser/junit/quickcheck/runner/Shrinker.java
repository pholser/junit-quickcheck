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

package com.pholser.junit.quickcheck.runner;

import java.util.List;
import java.util.Queue;

import com.pholser.junit.quickcheck.MinimalCounterexampleHook;
import com.pholser.junit.quickcheck.internal.ShrinkControl;
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
        ShrinkControl shrinkControl) {

        this.method = method;
        this.testClass = testClass;
        this.failure = failure;
        this.maxShrinks = shrinkControl.maxShrinks();
        this.maxShrinkDepth = shrinkControl.maxShrinkDepth() * method.getMethod().getParameterCount();
        this.maxShrinkTime = shrinkControl.maxShrinkTime();
        this.onMinimalCounterexample = shrinkControl.onMinimalCounterexample();
    }

    void shrink(
        List<PropertyParameterGenerationContext> params,
        Object[] args,
        long[] seeds)
        throws Throwable {

        ShrinkNode smallest =
            ShrinkNode.root(method, testClass, params, args, seeds, failure);
        Queue<ShrinkNode> nodes = new ShrinkNodeQueue(smallest.magnitude());
        nodes.addAll(smallest.shrinks());

        shrinkTimeout = System.currentTimeMillis() + maxShrinkTime;

        while (shouldContinueShrinking(nodes)) {
            ShrinkNode next = nodes.poll();

            boolean result = next.verifyProperty();
            ++shrinkAttempts;

            if (!result) {
                smallest = next;
                nodes.addAll(smallest.shrinks());
            }
        }

        handleMinimalCounterexample(smallest);
        throw smallest.fail(failure, args);
    }

    private void handleMinimalCounterexample(ShrinkNode counterexample) {
        Runnable repeat = () -> {
            try {
                counterexample.verifyProperty();
            } catch (Throwable ignored) {
            }
        };

        onMinimalCounterexample.handle(counterexample.args(), repeat);
    }

    private boolean shouldContinueShrinking(Queue<ShrinkNode> nodes) {
        return shrinkAttempts < maxShrinks
            && shrinkTimeout >= System.currentTimeMillis()
            && !nodes.isEmpty()
            && nodes.peek().depth() <= maxShrinkDepth;
    }
}

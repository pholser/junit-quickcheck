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

package com.pholser.junit.quickcheck.runner;

import java.util.Collections;
import java.util.List;

import com.pholser.junit.quickcheck.internal.generator.PropertyParameterGenerationContext;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;
import org.slf4j.Logger;

import static com.pholser.junit.quickcheck.runner.PropertyFalsified.*;
import static java.util.stream.Collectors.*;

final class ShrinkNode {
    private final FrameworkMethod method;
    private final TestClass testClass;
    private final List<PropertyParameterGenerationContext> params;
    private final Object[] args;
    private final long[] seeds;
    private final Logger logger;
    private final int argIndex;
    private final int depth;

    private AssertionError failure;

    private ShrinkNode(
        FrameworkMethod method,
        TestClass testClass,
        List<PropertyParameterGenerationContext> params,
        Object[] args,
        long[] seeds,
        Logger logger,
        int argIndex,
        int depth,
        AssertionError failure) {

        this.method = method;
        this.testClass = testClass;
        this.params = params;
        this.args = args;
        this.seeds = seeds;
        this.logger = logger;
        this.argIndex = argIndex;
        this.depth = depth;
        this.failure = failure;
    }

    Object[] getArgs() {
        return args;
    }

    static ShrinkNode root(
        FrameworkMethod method,
        TestClass testClass,
        List<PropertyParameterGenerationContext> params,
        Object[] args,
        long[] seeds,
        Logger logger,
        AssertionError failure) {

        return new ShrinkNode(
            method,
            testClass,
            params,
            args,
            seeds,
            logger,
            0,
            0,
            failure);
    }

    List<ShrinkNode> shrinks() {
        if (argIndex >= args.length)
            return Collections.emptyList();

        PropertyParameterGenerationContext param = params.get(argIndex);
        return param.shrink(args[argIndex]).stream()
            .filter(s -> !s.equals(args[argIndex]))
            .map(this::shrinkNodeFor)
            .collect(toList());
    }

    boolean verifyProperty() throws Throwable {
        boolean[] result = new boolean[1];

        property(result).verify();

        return result[0];
    }

    ShrinkNode advanceToNextArg() {
        return new ShrinkNode(
            method,
            testClass,
            params,
            args,
            seeds,
            logger, argIndex + 1,
            depth,
            failure);
    }

    AssertionError fail(AssertionError originalFailure, Object[] originalArgs) {
        return originalFailure == failure
            ? counterexampleFound(
                method.getName(),
                args,
                seeds,
                failure)
            : smallerCounterexampleFound(
                method.getName(),
                originalArgs,
                args,
                seeds,
                failure,
                originalFailure);
    }

    boolean mightBePast(ShrinkNode other) {
        return argIndex >= other.argIndex && depth >= other.depth;
    }

    boolean deeperThan(int max) {
        return depth > max;
    }

    private PropertyVerifier property(boolean[] result) throws InitializationError {
        return new PropertyVerifier(
            testClass,
            method,
            args,
            seeds,
            s -> result[0] = true,
            v -> result[0] = true,
            (e, repeatTestOption) -> {
                failure = e;
                result[0] = false;
            });
    }

    private ShrinkNode shrinkNodeFor(Object shrunk) {
        Object[] shrunkArgs = new Object[args.length];
        System.arraycopy(args, 0, shrunkArgs, 0, args.length);
        shrunkArgs[argIndex] = shrunk;

        return new ShrinkNode(
            method,
            testClass,
            params,
            shrunkArgs,
            seeds,
            logger,
            argIndex,
            depth + 1,
            failure);
    }
}

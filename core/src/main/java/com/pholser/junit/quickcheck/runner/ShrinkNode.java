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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import com.pholser.junit.quickcheck.internal.generator.PropertyParameterGenerationContext;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

import static java.util.Comparator.*;
import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;

import static com.pholser.junit.quickcheck.runner.PropertyFalsified.*;

final class ShrinkNode implements Comparable<ShrinkNode> {
    private final FrameworkMethod method;
    private final TestClass testClass;
    private final List<PropertyParameterGenerationContext> params;
    private final Object[] args;
    private final long[] seeds;
    private final int[] depths;
    private final int totalDepth;
    private final BigDecimal totalMagnitude;

    private AssertionError failure;

    private ShrinkNode(
        FrameworkMethod method,
        TestClass testClass,
        List<PropertyParameterGenerationContext> params,
        Object[] args,
        long[] seeds,
        int[] depths,
        AssertionError failure) {

        this.method = method;
        this.testClass = testClass;
        this.params = params;
        this.args = args;
        this.seeds = seeds;
        this.depths = depths;
        this.totalDepth = IntStream.of(depths).sum();
        this.totalMagnitude = computeTotalMagnitude();

        this.failure = failure;
    }

    static ShrinkNode root(
        FrameworkMethod method,
        TestClass testClass,
        List<PropertyParameterGenerationContext> params,
        Object[] args,
        long[] seeds,
        AssertionError failure) {

        return new ShrinkNode(
            method,
            testClass,
            params,
            args,
            seeds,
            new int[args.length],
            failure);
    }

    List<ShrinkNode> shrinks() {
        return IntStream.range(0, params.size())
            .mapToObj(i -> params.get(i).shrink(args[i]).stream()
                .filter(o -> !o.equals(args[i]))
                .map(o -> shrinkNodeFor(o, i)))
            .flatMap(identity())
            .collect(toList());
    }

    boolean verifyProperty() throws Throwable {
        boolean[] result = new boolean[1];

        property(result).verify();

        return result[0];
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

    Object[] args() {
        return args;
    }

    int depth() {
        return totalDepth;
    }

    BigDecimal magnitude() {
        return totalMagnitude;
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

    private ShrinkNode shrinkNodeFor(Object shrunk, int index) {
        Object[] shrunkArgs = new Object[args.length];
        System.arraycopy(args, 0, shrunkArgs, 0, args.length);
        shrunkArgs[index] = shrunk;

        int[] newDepths = new int[depths.length];
        System.arraycopy(depths, 0, newDepths, 0, depths.length);
        ++newDepths[index];

        return new ShrinkNode(
            method,
            testClass,
            params,
            shrunkArgs,
            seeds,
            newDepths,
            failure);
    }

    private BigDecimal computeTotalMagnitude() {
        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i < args.length; ++i) {
            BigDecimal magnitude = magnitudeAt(i);
            BigDecimal factor = BigDecimal.valueOf(2).pow(args.length - i - 1);
            total = total.add(magnitude.multiply(factor));
        }

        return total;
    }

    private BigDecimal magnitudeAt(int index) {
        return params.get(index).magnitude(args[index]);
    }

    @Override public boolean equals(Object o) {
        if (!(o instanceof ShrinkNode))
            return false;

        ShrinkNode other = (ShrinkNode) o;
        return Arrays.equals(args, other.args)
            && Arrays.equals(depths, other.depths);
    }

    @Override public int hashCode() {
        return Arrays.hashCode(args) ^ Arrays.hashCode(depths);
    }

    @Override public int compareTo(ShrinkNode other) {
        // Nodes at lesser depth compare less than nodes of greater depth.
        // Nodes at equal depth needs to compare their args one by one.
        // prefer larger-magnitude args before smaller.

        Comparator<ShrinkNode> comparison = comparing(ShrinkNode::depth);
        for (int i = 0; i < params.size(); ++i) {
            int index = i;
            Comparator<ShrinkNode> byMagnitude = comparing(s -> s.magnitudeAt(index));
            comparison = comparison.thenComparing(byMagnitude.reversed());
        }

        return comparison.compare(this, other);
    }
}

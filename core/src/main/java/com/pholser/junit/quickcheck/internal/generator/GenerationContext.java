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

package com.pholser.junit.quickcheck.internal.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.GeometricDistribution;
import com.pholser.junit.quickcheck.internal.ParameterContext;
import com.pholser.junit.quickcheck.internal.constraint.ConstraintEvaluator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.lang.Math.*;

@Deprecated
public class GenerationContext implements GenerationStatus {
    private final ParameterContext parameter;
    private final GeometricDistribution distro;
    private final ConstraintEvaluator evaluator;
    private final SourceOfRandomness random;
    private final Generator<?> generator;
    private int successfulEvaluations;
    private int discards;

    public GenerationContext(
        ParameterContext parameter,
        GeneratorRepository repository,
        GeometricDistribution distro,
        SourceOfRandomness random) {

        this.parameter = parameter;
        this.distro = distro;
        this.evaluator = new ConstraintEvaluator(parameter.constraint());
        this.random = random;
        this.generator = repository.produceGenerator(parameter.typeContext());
    }

    public Object generate(SourceOfRandomness random) {
        Object nextValue;

        for (nextValue = generator.generate(random, this);
            !evaluate(nextValue) && shouldContinue();
            nextValue = generator.generate(random, this));

        return nextValue;
    }

    private boolean evaluate(Object value) {
        evaluator.bind(value);
        boolean result = evaluator.evaluate();

        if (result)
            ++successfulEvaluations;
        else
            ++discards;

        if (tooManyDiscards())
            throw new DiscardRatioExceededException(parameter, discards, successfulEvaluations);

        return result;
    }

    public boolean shouldContinue() {
        return needMoreAttempts() && !tooManyDiscards();
    }

    private boolean needMoreAttempts() {
        return successfulEvaluations < parameter.sampleSize();
    }

    private boolean tooManyDiscards() {
        if (parameter.discardRatio() < 0)
            return true;
        if (parameter.discardRatio() == 0)
            return discards > parameter.sampleSize();

        return successfulEvaluations == 0
            ? discards > parameter.discardRatio()
            : (discards / successfulEvaluations) >= parameter.discardRatio();
    }

    @Override public int size() {
        int sample = distro.sampleWithMean(attempts() + 1, random);
        return min(sample, parameter.sampleSize());
    }

    @Override public int attempts() {
        return successfulEvaluations + discards;
    }

    public static class DiscardRatioExceededException extends RuntimeException {
        static final String MESSAGE_TEMPLATE =
            "For parameter with discard ratio [%d], %d unsuccessful values and %d successes"
                + " for a discard ratio of [%f]. Stopping.";

        private static final long serialVersionUID = Long.MIN_VALUE;

        DiscardRatioExceededException(ParameterContext parameter, int discards, int successfulEvaluations) {
            super(String.format(MESSAGE_TEMPLATE, parameter.discardRatio(), discards, successfulEvaluations,
                (double) discards / successfulEvaluations));
        }
    }
}

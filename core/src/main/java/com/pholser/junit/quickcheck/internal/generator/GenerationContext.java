/*
 The MIT License

 Copyright (c) 2010-2013 Paul R. Holser, Jr.

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
import com.pholser.junit.quickcheck.internal.ParameterContext;
import com.pholser.junit.quickcheck.internal.constraint.ConstraintEvaluator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class GenerationContext implements GenerationStatus {
    private final ParameterContext parameter;
    private final GeneratorRepository repository;
    private final ConstraintEvaluator evaluator;
    private int successfulEvaluations;
    private int discards;

    public GenerationContext(ParameterContext parameter, GeneratorRepository repository) {
        this.parameter = parameter;
        this.repository = repository;
        this.evaluator = new ConstraintEvaluator(parameter.constraint());
    }

    public Object generate(SourceOfRandomness random) {
        return generateUsing(decideGenerator(), random);
    }

    private Object generateUsing(Generator<?> generator, SourceOfRandomness random) {
        Object nextValue;

        do {
            nextValue = generator.generate(random, this);
            if (evaluate(nextValue))
                break;
        } while (shouldContinue());

        return nextValue;
    }

    private Generator<?> decideGenerator() {
        Generator<?> generator = parameter.explicitGenerator();
        if (generator == null)
            generator = repository.generatorFor(parameter.parameterType());

        generator.configure(parameter.configurations());

        return generator;
    }

    private boolean evaluate(Object value) {
        evaluator.bind("_", value);
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
        return successfulEvaluations;
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

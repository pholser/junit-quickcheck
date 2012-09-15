package com.pholser.junit.quickcheck.internal.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.ParameterContext;
import com.pholser.junit.quickcheck.internal.constraint.ConstraintEvaluator;
import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;
import org.junit.internal.AssumptionViolatedException;

public class GenerationContext implements GenerationStatus {
    private static final String DISCARD_RATIO_MESSAGE =
        "For parameter with discard ratio [%d], %d unsuccessful values and %d successes for a discard ratio of [%f]."
            + " Stopping.";

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
        Object nextValue = null;

        while (shouldContinue()) {
            nextValue = generator.generate(random, this);
            if (evaluate(nextValue))
                break;
        }

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
        boolean result = evaluator.evaluate(value);

        if (result)
            ++successfulEvaluations;
        else
            ++discards;

        if (tooManyDiscards()) {
            throw new AssumptionViolatedException(
                String.format(DISCARD_RATIO_MESSAGE, parameter.discardRatio(), discards, successfulEvaluations,
                    (double) discards / successfulEvaluations));
        }

        return result;
    }

    public boolean shouldContinue() {
        return needMoreAttempts() && !tooManyDiscards();
    }

    private boolean needMoreAttempts() {
        return successfulEvaluations < parameter.sampleSize();
    }

    private boolean tooManyDiscards() {
        return parameter.discardRatio() < 0
            || parameter.discardRatio() > 0 && (discards / successfulEvaluations) > parameter.discardRatio();
    }

    @Override
    public int size() {
        return successfulEvaluations;
    }

    @Override
    public int successes() {
        return successfulEvaluations;
    }

    @Override
    public int discards() {
        return discards;
    }
}

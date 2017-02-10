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

package com.pholser.junit.quickcheck.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.pholser.junit.quickcheck.Pair;
import com.pholser.junit.quickcheck.internal.Items;
import com.pholser.junit.quickcheck.internal.Weighted;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/**
 * Represents a strategy for generating random values.
 *
 * @param <T> type of values generated
 */
@FunctionalInterface
public interface Gen<T> {
    /**
     * Generates a value, possibly influenced by a source of randomness and
     * metadata about the generation.
     *
     * @param random source of randomness to be used when generating the value
     * @param status an object that can be used to influence the generated
     * value. For example, generating lists can use the {@link
     * GenerationStatus#size() size} method to generate lists with a given
     * number of elements.
     * @return the generated value
     */
    T generate(SourceOfRandomness random, GenerationStatus status);

    /**
     * Gives a generation strategy that produces a random value by having this
     * strategy produce a random value, then applying the given function to
     * that value, and returning the result.
     *
     * @param <U> type of values produced by the mapped generation strategy
     * @param mapper function that converts receiver's random values to
     * result's values
     * @return a new generation strategy
     */
    default <U> Gen<U> map(Function<? super T, ? extends U> mapper) {
        return (random, status) -> mapper.apply(generate(random, status));
    }

    /**
     * Gives a generation strategy that produces a random value by having this
     * strategy produce a random value, then applying the given function to
     * that value, and asking the result to produce a value.
     *
     * @param <U> type of values produced by the mapped generation strategy
     * @param mapper function that converts receiver's random values to
     * another kind of generation strategy
     * @return a new generation strategy
     */
    default <U> Gen<U> flatMap(Function<? super T, ? extends Gen<? extends U>> mapper) {
        return (random, status) ->
            mapper.apply(generate(random, status)).generate(random, status);
    }

    /**
     * Gives a generation strategy that produces a random value by having this
     * strategy produce random values until one satisfies the given condition,
     * then using that value as the result.
     *
     * @param condition a condition the generated value is to meet
     * @return a new generation strategy
     */
    default Gen<T> filter(Predicate<? super T> condition) {
        return (random, status) -> {
            T next = generate(random, status);
            while (!condition.test(next)) {
                next = generate(random, status);
            }
            return next;
        };
    }

    /**
     * <p>Gives a generation strategy that produces a random value by having
     * this strategy produce a random value and wrapping it in an
     * {@link Optional}; if the value meets the given condition, the wrapping
     * optional will be {@linkplain Optional#isPresent() present}.</p>
     *
     * <p>If the value meets the condition but is {@code null}, the wrapping
     * optional will not be considered
     * {@linkplain Optional#isPresent() present}.</p>
     *
     * @param condition a condition the generated value is to meet
     * @return a new generation strategy
     */
    default Gen<Optional<T>> filterOptional(Predicate<? super T> condition) {
        return (random, status) -> {
            T next = generate(random, status);
            return condition.test(next)
                ? Optional.ofNullable(next)
                : Optional.empty();
        };
    }

    default Gen<List<T>> times(int times) {
        if (times < 0)
            throw new IllegalArgumentException("negative times: " + times);

        return (random, status) -> {
            List<T> values = new ArrayList<>();
            for (int i = 0; i < times; ++i)
                values.add(generate(random, status));
            return values;
        };
    }

    /**
     * Gives a generation strategy that produces the given value, always.
     *
     * @param <U> type of values produced by the resulting strategy
     * @param constant the value to be returned by the resulting strategy
     * @return a new generation strategy
     */
    static <U> Gen<U> pure(U constant) {
        return (random, status) -> constant;
    }

    /**
     * Gives a generation strategy that produces a random value by choosing
     * one of the given values at random with (approximately) equal
     * probability.
     *
     * @param <U> type of values produced by the resulting strategy
     * @param first first possible choice
     * @param rest the other possible choices
     * @return a new generation strategy
     */
    @SafeVarargs static <U> Gen<U> oneOf(U first, U... rest) {
        List<U> choices = new ArrayList<>();
        choices.add(first);
        Collections.addAll(choices, rest);

        return (random, status) -> Items.choose(choices, random);
    }

    /**
     * Gives a generation strategy that produces a random value by choosing
     * one of the given generators at random with (approximately) equal
     * probability, and having it generate a value.
     *
     * @param <U> type of values produced by the resulting strategy
     * @param first first possible generator choice
     * @param rest the other possible generator choices
     * @return a new generation strategy
     */
    @SafeVarargs static <U> Gen<U> oneOf(
        Gen<? extends U> first,
        Gen<? extends U>... rest) {

        List<Gen<? extends U>> choices = new ArrayList<>();
        choices.add(first);
        Collections.addAll(choices, rest);

        return (random, status) ->
            Items.choose(choices, random).generate(random, status);
    }

    /**
     * Gives a generation strategy that produces a random value by choosing
     * one of the given generators at random with probability in proportion
     * to their given weights, and having it generate a value.
     *
     * @param <U> type of values produced by the resulting strategy
     * @param first first possible (weighted) generator choice
     * @param rest the other possible (weighted) generator choices
     * @return a new generation strategy
     */
    @SafeVarargs static <U> Gen<U> frequency(
        Pair<Integer, Gen<? extends U>> first,
        Pair<Integer, Gen<? extends U>>... rest) {

        List<Pair<Integer, Gen<? extends U>>> pairs = new ArrayList<>();
        pairs.add(first);
        Collections.addAll(pairs, rest);

        List<Weighted<Gen<? extends U>>> weighted =
            pairs.stream()
                .map(p -> new Weighted<Gen<? extends U>>(p.second, p.first))
                .collect(Collectors.toList());

        return (random, status) ->
            Items.chooseWeighted(weighted, random).generate(random, status);
    }

    /**
     * Helper for making a weighted generator indicator for
     * {@link #frequency(Pair, Pair[])}.
     *
     * @param <U> type of values produced by the given generation strategy
     * @param weight an integer weight
     * @param generator a generator
     * @return a weight-generator pair
     */
    static <U> Pair<Integer, Gen<? extends U>> freq(
        int weight,
        Gen<? extends U> generator) {

        return new Pair<>(weight, generator);
    }
}

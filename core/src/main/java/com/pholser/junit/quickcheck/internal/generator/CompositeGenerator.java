/*
 The MIT License

 Copyright (c) 2010-2015 Paul R. Holser, Jr.

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
import com.pholser.junit.quickcheck.generator.GeneratorConfigurationException;
import com.pholser.junit.quickcheck.internal.Items;
import com.pholser.junit.quickcheck.internal.Weighted;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CompositeGenerator extends Generator<Object> {
    private final List<Weighted<Generator<?>>> composed;

    public CompositeGenerator(List<Weighted<Generator<?>>> composed) {
        super(Object.class);

        this.composed = new ArrayList<>(composed);
    }

    @Override public Object generate(SourceOfRandomness random, GenerationStatus status) {
        Generator<?> choice = Items.chooseWeighted(composed, random);
        return choice.generate(random, status);
    }

    @Override public boolean canShrink(Object larger) {
        return composed.stream()
            .map(w -> w.item)
            .anyMatch(g -> g.canShrink(larger));
    }

    @Override public List<Object> doShrink(SourceOfRandomness random, Object larger) {
        List<Weighted<Generator<?>>> shrinkers =
            composed.stream()
                .filter(w -> w.item.canShrink(larger))
                .collect(Collectors.toList());

        Generator<?> choice = Items.chooseWeighted(shrinkers, random);
        return new ArrayList<>(choice.shrink(random, larger));
    }

    public Generator<?> composed(int index) {
        return composed.get(index).item;
    }

    public int numberOfComposedGenerators() {
        return composed.size();
    }

    @Override public void provideRepository(GeneratorRepository provided) {
        super.provideRepository(provided);

        for (Weighted<Generator<?>> each : composed)
            each.item.provideRepository(provided);
    }

    @Override public void configure(AnnotatedType annotatedType) {
        List<Weighted<Generator<?>>> candidates = new ArrayList<>(composed);

        for (Iterator<Weighted<Generator<?>>> it = candidates.iterator(); it.hasNext();) {
            try {
                it.next().item.configure(annotatedType);
            } catch (GeneratorConfigurationException e) {
                it.remove();
            }
        }

        installCandidates(candidates, annotatedType);
    }

    @Override public void configure(AnnotatedElement element) {
        List<Weighted<Generator<?>>> candidates = new ArrayList<>(composed);

        for (Iterator<Weighted<Generator<?>>> it = candidates.iterator(); it.hasNext();) {
            try {
                it.next().item.configure(element);
            } catch (GeneratorConfigurationException e) {
                it.remove();
            }
        }

        installCandidates(candidates, element);
    }

    private void installCandidates(
        List<Weighted<Generator<?>>> candidates,
        AnnotatedElement element) {

        if (candidates.isEmpty()) {
            throw new GeneratorConfigurationException(
                String.format(
                    "None of the candidate generators %s"
                        + " understands all of the configuration annotations %s",
                    candidateGeneratorDescriptions(),
                    configurationAnnotationNames(element)));
        }

        composed.clear();
        composed.addAll(candidates);
    }

    private String candidateGeneratorDescriptions() {
        return composed.stream()
            .map(w -> w.item.getClass().getName())
            .collect(Collectors.toList())
            .toString();
    }

    private static List<String> configurationAnnotationNames(AnnotatedElement element) {
        return configurationAnnotationsOn(element).stream()
            .map(a -> a.annotationType().getName())
            .collect(Collectors.toList());
    }
}

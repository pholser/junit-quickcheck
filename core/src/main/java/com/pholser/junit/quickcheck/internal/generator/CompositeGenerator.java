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

import java.lang.reflect.AnnotatedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CompositeGenerator extends Generator<Object> {
    private final List<Weighted<Generator<?>>> components;

    public CompositeGenerator(List<Weighted<Generator<?>>> components) {
        super(Object.class);

        this.components = new ArrayList<>(components);
    }

    @Override public Object generate(SourceOfRandomness random, GenerationStatus status) {
        Generator<?> choice = Items.chooseWeighted(components, random);
        return choice.generate(random, status);
    }

    public Generator<?> componentGenerator(int index) {
        return components.get(index).item;
    }

    public int numberOfComponentGenerators() {
        return components.size();
    }

    @Override public void provideRepository(GeneratorRepository provided) {
        super.provideRepository(provided);

        for (Weighted<Generator<?>> each : components)
            each.item.provideRepository(provided);
    }

    @Override public void configure(AnnotatedType annotatedType) {
        for (Iterator<Weighted<Generator<?>>> it = components.iterator(); it.hasNext();) {
            try {
                it.next().item.configure(annotatedType);
            } catch (GeneratorConfigurationException e) {
                it.remove();
            }
        }

        if (components.isEmpty()) {
            throw new GeneratorConfigurationException(
                String.format(
                    "No generator that can produce values of type %s"
                        + " understands all of the configuration annotations %s",
                    annotatedType.getType().getTypeName(),
                    configurationAnnotationNames(annotatedType)));
        }
    }

    private static List<String> configurationAnnotationNames(AnnotatedType annotatedType) {
        return configurationAnnotationsOn(annotatedType).stream()
            .map(a -> a.annotationType().getName())
            .collect(Collectors.toList());
    }
}

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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class CompositeGenerator extends Generator<Object> {
    private final List<Generator<?>> components;

    public CompositeGenerator(List<Generator<?>> components) {
        super(Object.class);

        this.components = new ArrayList<Generator<?>>(components);
    }

    @Override public Object generate(SourceOfRandomness random, GenerationStatus status) {
        Generator<?> generator = components.size() == 1
            ? components.get(0)
            : components.get(random.nextInt(0, components.size() - 1));

        return generator.generate(random, status);
    }

    public Generator<?> componentGenerator(int index) {
        return components.get(index);
    }

    public int numberOfComponentGenerators() {
        return components.size();
    }

    @Override public void configure(Map<Class<? extends Annotation>, Annotation> configurationsByType) {
        for (Generator<?> each : components)
            each.configure(configurationsByType);
    }
}

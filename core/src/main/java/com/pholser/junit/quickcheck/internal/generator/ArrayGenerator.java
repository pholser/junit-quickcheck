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
import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.internal.Ranges;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Array;
import java.util.List;

import static com.pholser.junit.quickcheck.internal.Ranges.Type.INTEGRAL;
import static com.pholser.junit.quickcheck.internal.Ranges.checkRange;
import static com.pholser.junit.quickcheck.internal.Reflection.*;

public class ArrayGenerator extends Generator<Object> {
    private final Class<?> componentType;
    private final Generator<?> component;
    private Size lengthRange;

    public ArrayGenerator(Class<?> componentType, Generator<?> component) {
        super(Object.class);

        this.componentType = componentType;
        this.component = component;
    }

    /**
     * Tells this generator to produce values with a length within a specified minimum (inclusive)
     * and/or maximum (exclusive) with uniform distribution.
     *
     * @param lengthRange annotation that gives the length constraints
     */
    public void configure(Size lengthRange) {
        this.lengthRange = lengthRange;
        checkRange(INTEGRAL, lengthRange.min(), lengthRange.max());
    }

    @Override public Object generate(SourceOfRandomness random, GenerationStatus status) {
        int length = length(random, status);
        Object array = Array.newInstance(componentType, length);

        for (int i = 0; i < length; ++i)
            Array.set(array, i, component.generate(random, status));

        return array;
    }

    private int length(SourceOfRandomness random, GenerationStatus status) {
        return lengthRange != null
            ? random.nextInt(lengthRange.min(), lengthRange.max())
            : status.size();
    }

    @Override public void provideRepository(GeneratorRepository provided) {
        super.provideRepository(provided);

        component.provideRepository(provided);
    }

    @Override public void configure(AnnotatedType annotatedType) {
        super.configure(annotatedType);

        component.configure(annotatedComponentTypes(annotatedType).get(0));
    }

    public Generator<?> componentGenerator() {
        return component;
    }
}

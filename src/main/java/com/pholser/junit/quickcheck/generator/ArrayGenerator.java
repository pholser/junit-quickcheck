/*
 The MIT License

 Copyright (c) 2010-2011 Paul R. Holser, Jr.

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

import java.lang.reflect.Array;

import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;

public class ArrayGenerator extends Generator<Object> {
    private final Class<?> componentType;
    private final Generator<?> componentGenerator;

    public ArrayGenerator(Class<?> componentType, Generator<?> componentGenerator) {
        super(Object.class);

        this.componentType = componentType;
        this.componentGenerator = componentGenerator;
    }

    @Override
    public Object generate(SourceOfRandomness random, int size) {
        Object array = Array.newInstance(componentType, size);
        for (int i = 0; i < size; ++i)
            Array.set(array, i, componentGenerator.generate(random, size));

        return array;
    }
}

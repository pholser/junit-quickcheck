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

package com.pholser.junit.parameters.internal;

import java.lang.reflect.Array;

public class ArrayExtractor implements RandomValueExtractor<Object> {
    private final Class<?> componentType;
    private final RandomValueExtractor<?> componentExtractor;

    public ArrayExtractor(Class<?> componentType, RandomValueExtractor<?> componentExtractor) {
        this.componentType = componentType;
        this.componentExtractor = componentExtractor;
    }

    @Override
    public Object extract(SourceOfRandomness random) {
        int length = random.nextInt(0, 100);
        Object array = Array.newInstance(componentType, length);
        for (int i = 0; i < length; ++i)
            Array.set(array, i, componentExtractor.extract(random));
        return array;
    }
}

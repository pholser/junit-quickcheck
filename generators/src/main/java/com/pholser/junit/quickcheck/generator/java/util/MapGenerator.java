/*
 The MIT License

 Copyright (c) 2010-2014 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.generator.java.util;

import java.util.Map;

import com.pholser.junit.quickcheck.generator.ComponentizedGenerator;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/**
 * <p>Base class for generators of {@link Map}s.</p>
 *
 * <p>The generated map has a number of entries no larger than {@link GenerationStatus#size()}. The individual keys
 * and values will have types corresponding to the theory parameter's type arguments.</p>
 *
 * @param <T> the type of map generated
 */
public abstract class MapGenerator<T extends Map> extends ComponentizedGenerator<T> {
    protected MapGenerator(Class<T> type) {
        super(type);
    }

    @SuppressWarnings("unchecked")
    @Override public T generate(SourceOfRandomness random, GenerationStatus status) {
        T items = emptyMap();

        for (int i = 0; i < status.size(); ++i) {
            Object key = componentGenerators().get(0).generate(random, status);
            Object value = componentGenerators().get(1).generate(random, status);
            if (okToAdd(key, value))
                items.put(key, value);
        }

        return items;
    }

    @Override public int numberOfNeededComponents() {
        return 2;
    }

    @Override public boolean canRegisterAsType(Class<?> type) {
        return !Object.class.equals(type);
    }

    protected abstract T emptyMap();

    protected boolean okToAdd(Object key, Object value) {
        return true;
    }
}

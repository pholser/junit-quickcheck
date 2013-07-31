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

package com.pholser.junit.quickcheck.generator.java.util;

import java.util.HashMap;

import com.pholser.junit.quickcheck.generator.ComponentizedGenerator;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/**
 * <p>Produces values for theory parameters of type {@link HashMap}.</p>
 *
 * <p>The generated map has a number of entries no larger than
 * {@link com.pholser.junit.quickcheck.generator.GenerationStatus#size()}. The individual keys and values will have
 * types corresponding to the theory parameter's type arguments.</p>
 */
public class HashMapGenerator extends ComponentizedGenerator<HashMap> {
    public HashMapGenerator() {
        super(HashMap.class);
    }

    @Override public HashMap<?, ?> generate(SourceOfRandomness random, GenerationStatus status) {
        HashMap<Object, Object> items = new HashMap<Object, Object>();

        for (int i = 0; i < status.size(); ++i) {
            Object key = componentGenerators().get(0).generate(random, status);
            Object value = componentGenerators().get(1).generate(random, status);
            items.put(key, value);
        }

        return items;
    }

    @Override public int numberOfNeededComponents() {
        return 2;
    }
}

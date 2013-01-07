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

package com.pholser.junit.quickcheck.generator;

import java.util.HashSet;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/**
 * <p>Produces values for theory parameters of type {@link java.util.HashSet}.</p>
 *
 * <p>The generated list has a number of elements decided by
 * {@link com.pholser.junit.quickcheck.generator.GenerationStatus#size()}. The individual elements will have a type
 * corresponding to the theory parameter's type argument.</p>
 */
public class HashSetGenerator extends ComponentizedGenerator<HashSet> {
    public HashSetGenerator() {
        super(HashSet.class);
    }

    @Override public HashSet<?> generate(SourceOfRandomness random, GenerationStatus status) {
        HashSet<Object> items = new HashSet<Object>();

        for (int itemsAdded = 0; itemsAdded < status.size(); ++itemsAdded)
            items.add(componentGenerators().get(0).generate(random, status));

        return items;
    }

    @Override public int numberOfNeededComponents() {
        return 1;
    }
}

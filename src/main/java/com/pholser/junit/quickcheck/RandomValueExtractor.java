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

package com.pholser.junit.quickcheck;

import java.util.List;

import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;

import static java.util.Arrays.*;
import static java.util.Collections.*;

public abstract class RandomValueExtractor<T> {
    private final List<Class<T>> types;

    @SuppressWarnings("unchecked")
    protected RandomValueExtractor(Class<T> type) {
        this(asList(type));
    }

    protected RandomValueExtractor(List<Class<T>> types) {
        this.types = types;
    }

    public List<Class<T>> types() {
        return unmodifiableList(types);
    }

    public abstract T extract(SourceOfRandomness random, int size);

    public boolean hasComponents() {
        return false;
    }

    public void addComponentExtractors(List<RandomValueExtractor<?>> extractors) {
        // do nothing by default
    }
}

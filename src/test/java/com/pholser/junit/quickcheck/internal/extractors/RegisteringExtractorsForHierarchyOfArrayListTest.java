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

package com.pholser.junit.quickcheck.internal.extractors;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

import com.pholser.junit.quickcheck.RandomValueExtractor;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.collect.Lists.*;
import static com.pholser.junit.quickcheck.internal.extractors.Extractors.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

public class RegisteringExtractorsForHierarchyOfArrayListTest {
    private ExtractorRepository repo;
    private ArrayListExtractor extractor;

    @Before
    public void setUp() {
        repo = new ExtractorRepository();

        extractor = new ArrayListExtractor();
        List<RandomValueExtractor<?>> extractors = newArrayList();
        extractors.add(extractor);
        extractors.add(new IntegerExtractor());

        repo.add(extractors);
    }

    @Test
    public void abstractList() {
        RandomValueExtractor<?> result = repo.extractorFor(AbstractList.class);

        assertExtractors(result, extractor.getClass());
    }

    @Test
    public void list() {
        RandomValueExtractor<?> result = repo.extractorFor(List.class);

        assertExtractors(result, extractor.getClass());
    }

    @Test
    public void randomAccess() {
        RandomValueExtractor<?> result = repo.extractorFor(RandomAccess.class);

        assertExtractors(result, extractor.getClass());
    }

    @Test
    public void cloneable() {
        RandomValueExtractor<?> result = repo.extractorFor(Cloneable.class);

        assertExtractors(result, extractor.getClass());
    }

    @Test
    public void serializable() {
        RandomValueExtractor<?> result = repo.extractorFor(Serializable.class);

        assertExtractors(result, extractor.getClass(), IntegerExtractor.class);
    }

    @Test
    public void abstractCollection() {
        RandomValueExtractor<?> result = repo.extractorFor(AbstractCollection.class);

        assertExtractors(result, extractor.getClass());
    }

    @Test
    public void collection() {
        RandomValueExtractor<?> result = repo.extractorFor(Collection.class);

        assertExtractors(result, extractor.getClass());
    }

    @Test
    public void iterable() {
        RandomValueExtractor<?> result = repo.extractorFor(Iterable.class);

        assertExtractors(result, extractor.getClass());
    }
}

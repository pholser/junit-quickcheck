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
import java.math.BigDecimal;
import java.util.List;

import com.pholser.junit.quickcheck.RandomValueExtractor;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.collect.Lists.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

public class RegisteringExtractorsForHierarchyOfBigDecimalTest {
    private ExtractorRepository repo;
    private BigDecimalExtractor extractor;

    @Before
    public void setUp() {
        repo = new ExtractorRepository();

        extractor = new BigDecimalExtractor();
        List<RandomValueExtractor<?>> extractors = newArrayList();
        extractors.add(extractor);

        repo.add(extractors);
    }

    @Test
    public void bigDecimal() {
        RandomValueExtractor<?> result = repo.extractorFor(BigDecimal.class);

        assertSingleExtractor(result);
    }

    @Test
    public void comparable() {
        RandomValueExtractor<?> result = repo.extractorFor(Comparable.class);

        assertSingleExtractor(result);
    }

    @Test
    public void serializable() {
        RandomValueExtractor<?> result = repo.extractorFor(Serializable.class);

        assertSingleExtractor(result);
    }

    @Test
    public void number() {
        RandomValueExtractor<?> result = repo.extractorFor(Number.class);

        assertSingleExtractor(result);
    }

    @Test
    public void object() {
        RandomValueExtractor<?> result = repo.extractorFor(Object.class);

        assertSingleExtractor(result);
    }

    private void assertSingleExtractor(RandomValueExtractor<?> result) {
        assumeThat(result, is(CompositeRandomValueExtractor.class));

        CompositeRandomValueExtractor composite = (CompositeRandomValueExtractor) result;
        assertEquals(1, composite.components.size());
        assertThat(composite.components.get(0), is(extractor.getClass()));
    }
}

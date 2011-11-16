package com.pholser.junit.quickcheck.internal.extractors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.AbstractList;
import java.util.List;

import com.pholser.junit.quickcheck.RandomValueExtractor;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.collect.Lists.*;
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
        extractors.add(new ObjectExtractor());

        repo.add(extractors);
    }

    @Test
    public void abstractList() {
        RandomValueExtractor<?> result = repo.extractorFor(AbstractList.class);

        assertSingleExtractor(result);
    }

    @Test
    public void list() {
        RandomValueExtractor<?> result = repo.extractorFor(List.class);

        assertSingleExtractor(result);
    }

    private void assertSingleExtractor(RandomValueExtractor<?> result) {
        assumeThat(result, is(CompositeRandomValueExtractor.class));

        CompositeRandomValueExtractor composite = (CompositeRandomValueExtractor) result;
        assertEquals(1, composite.components.size());
        assertThat(composite.components.get(0), is(extractor.getClass()));
    }
}

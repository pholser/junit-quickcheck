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

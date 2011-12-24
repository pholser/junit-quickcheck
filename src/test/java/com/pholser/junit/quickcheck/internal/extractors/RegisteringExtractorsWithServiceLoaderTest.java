package com.pholser.junit.quickcheck.internal.extractors;

import com.pholser.junit.quickcheck.test.extractors.AnotherShortExtractor;
import com.pholser.junit.quickcheck.test.extractors.Foo;
import com.pholser.junit.quickcheck.test.extractors.FooExtractor;
import org.junit.Before;
import org.junit.Test;

import static com.pholser.junit.quickcheck.internal.extractors.Extractors.*;

public class RegisteringExtractorsWithServiceLoaderTest {
    private ExtractorRepository repo;

    @Before
    public void setUp() {
        repo = new ExtractorRepository();
        repo.add(new BasicExtractorSource()).add(new ServiceLoaderExtractorSource());
    }

    @Test
    public void bringsInTypesOtherThanBasicTypes() {
        assertExtractors(repo.extractorFor(Foo.class), FooExtractor.class);
    }

    @Test
    public void bringsInTypesToSupplementBasicTypes() {
        assertExtractors(repo.extractorFor(short.class), ShortExtractor.class, AnotherShortExtractor.class);
    }
}

package com.pholser.junit.quickcheck.internal.generator;

import com.pholser.junit.quickcheck.generator.ShortGenerator;
import com.pholser.junit.quickcheck.test.generator.AnotherShortGenerator;
import com.pholser.junit.quickcheck.test.generator.Foo;
import com.pholser.junit.quickcheck.test.generator.FooGenerator;
import org.junit.Before;
import org.junit.Test;

import static com.pholser.junit.quickcheck.internal.generator.Generators.*;

public class RegisteringGeneratorsWithServiceLoaderTest {
    private GeneratorRepository repo;

    @Before
    public void setUp() {
        repo = new GeneratorRepository();
        repo.add(new BasicGeneratorSource()).add(new ServiceLoaderGeneratorSource());
    }

    @Test
    public void bringsInTypesOtherThanBasicTypes() {
        assertExtractors(repo.generatorFor(Foo.class), FooGenerator.class);
    }

    @Test
    public void bringsInTypesToSupplementBasicTypes() {
        assertExtractors(repo.generatorFor(short.class), ShortGenerator.class, AnotherShortGenerator.class);
    }
}

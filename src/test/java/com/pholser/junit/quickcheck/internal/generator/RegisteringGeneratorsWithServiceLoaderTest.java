package com.pholser.junit.quickcheck.internal.generator;

import com.pholser.junit.quickcheck.generator.ShortGenerator;
import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.test.generator.AnotherShortGenerator;
import com.pholser.junit.quickcheck.test.generator.Foo;
import com.pholser.junit.quickcheck.test.generator.FooGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.pholser.junit.quickcheck.internal.generator.Generators.*;

public class RegisteringGeneratorsWithServiceLoaderTest {
    private GeneratorRepository repo;
    @Mock private SourceOfRandomness random;

    @Before
    public void beforeEach() {
        MockitoAnnotations.initMocks(this);

        repo = new GeneratorRepository(random);
        repo.add(new BasicGeneratorSource()).add(new ServiceLoaderGeneratorSource());
    }

    @Test
    public void bringsInTypesOtherThanBasicTypes() {
        assertGenerators(repo.generatorFor(Foo.class), FooGenerator.class);
    }

    @Test
    public void bringsInTypesToSupplementBasicTypes() {
        assertGenerators(repo.generatorFor(short.class), ShortGenerator.class, AnotherShortGenerator.class);
    }
}

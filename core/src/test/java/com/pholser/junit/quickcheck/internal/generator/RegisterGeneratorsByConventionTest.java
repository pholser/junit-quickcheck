package com.pholser.junit.quickcheck.internal.generator;

import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.GeometricDistribution;
import com.pholser.junit.quickcheck.internal.ParameterTypeContext;
import com.pholser.junit.quickcheck.internal.generator.conventiontestclasses.Convention;
import com.pholser.junit.quickcheck.internal.generator.conventiontestclasses.GeneratesOtherTypes;
import com.pholser.junit.quickcheck.internal.generator.conventiontestclasses.NotAGenerator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class RegisterGeneratorsByConventionTest {
    private GeneratorRepository repo;
    private SourceOfRandomness random;

    @Before
    public void setupRepository() {
        random = new SourceOfRandomness(new Random());
        repo = new GeneratorRepository(random);
    }

    @Test
    public void canGenerateValues() {
        Generator<?> generator =
            repo.generatorFor(ParameterTypeContext.forClass(Convention.class));

        assertNotNull(generator);
        assertThat(
            generator.generate(
                random,
                new SimpleGenerationStatus(new GeometricDistribution(), random, 1)),
            instanceOf(Convention.class));
    }

    @Test
    public void classNameFollowsConventionButNotAGenerator() {
        assertThatNoGeneratorCanBeFound(NotAGenerator.class);
    }

    @Test
    public void noValueTypeMatch() {
        assertThatNoGeneratorCanBeFound(GeneratesOtherTypes.class);
    }

    @Test
    public void notAClassFollowingNamingConvention() {
        assertThatNoGeneratorCanBeFound(this.getClass());
    }

    private void assertThatNoGeneratorCanBeFound(Class<?> valueClass) {
        IllegalArgumentException ex =
            assertThrows(
                IllegalArgumentException.class,
                () -> repo.generatorFor(
                    ParameterTypeContext.forClass(valueClass)));

        assertThat(
            ex.getMessage(),
            containsString(
                "Cannot find generator for " + valueClass.getName()));
    }
}

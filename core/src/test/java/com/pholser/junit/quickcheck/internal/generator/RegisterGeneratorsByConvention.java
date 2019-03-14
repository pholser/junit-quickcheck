package com.pholser.junit.quickcheck.internal.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.ParameterTypeContext;
import com.pholser.junit.quickcheck.internal.generator.conventiontestclasses.GeneratesOtherTypes;
import com.pholser.junit.quickcheck.internal.generator.conventiontestclasses.Convention;
import com.pholser.junit.quickcheck.internal.generator.conventiontestclasses.NotAGenerator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RegisterGeneratorsByConvention {

    private GeneratorRepository repo;

    @Mock private SourceOfRandomness random;
    @Mock private GenerationStatus generationStatus;

    @Before
    public void setupRepository() {
        repo = new GeneratorRepository(random);
    }

    @Test
    public void can_generate_values() {
        Generator<?> generator = repo.generatorFor(new ParameterTypeContext(Convention.class));
        assertNotNull(generator);
        assertTrue(generator.generate(random, generationStatus) instanceof Convention);
    }

    private void assertThatNoGeneratorCanBeFound(Class<?> valueClass) {
        try {
            repo.generatorFor(new ParameterTypeContext(valueClass));
            fail("Shouldn't have found a suitable generator for: " + valueClass.getSimpleName());
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Cannot find generator for " + valueClass.getName()));
        }
    }

    @Test
    public void does_not_find_a_generator_even_if_the_class_name_would_follow_the_convention() {
        assertThatNoGeneratorCanBeFound(NotAGenerator.class);
    }

    @Test
    public void does_not_find_a_generator_if_the_types_it_generates_values_for_does_not_match() {
        assertThatNoGeneratorCanBeFound(GeneratesOtherTypes.class);
    }

    @Test
    public void does_not_find_a_generator_if_there_is_no_class_with_the_convention_following_name() {
        assertThatNoGeneratorCanBeFound(this.getClass());
    }
}

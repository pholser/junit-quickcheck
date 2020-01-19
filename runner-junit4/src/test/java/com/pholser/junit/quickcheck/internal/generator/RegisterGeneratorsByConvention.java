package com.pholser.junit.quickcheck.internal.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.ParameterTypeContext;
import com.pholser.junit.quickcheck.internal.generator.conventiontestclasses.GeneratesOtherTypes;
import com.pholser.junit.quickcheck.internal.generator.conventiontestclasses.Convention;
import com.pholser.junit.quickcheck.internal.generator.conventiontestclasses.NotAGenerator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.*;

@RunWith(MockitoJUnitRunner.class)
public class RegisterGeneratorsByConvention {
    @Rule public final ExpectedException thrown = none();

    private GeneratorRepository repo;
    @Mock private SourceOfRandomness random;
    @Mock private GenerationStatus generationStatus;

    @Before
    public void setupRepository() {
        repo = new GeneratorRepository(random);
    }

    @Test
    public void can_generate_values() {
        Generator<?> generator =
            repo.generatorFor(ParameterTypeContext.forClass(Convention.class));

        assertNotNull(generator);
        assertThat(
            generator.generate(random, generationStatus),
            instanceOf(Convention.class));
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

    private void assertThatNoGeneratorCanBeFound(Class<?> valueClass) {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(
            "Cannot find generator for " + valueClass.getName());

        repo.generatorFor(ParameterTypeContext.forClass(valueClass));
    }
}

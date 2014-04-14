package com.pholser.junit.quickcheck.generator.java.util;

import com.pholser.junit.quickcheck.Generating;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.generator.CoreTheoryParameterTest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.pholser.junit.quickcheck.Generating.*;
import static com.pholser.junit.quickcheck.generator.java.util.RFC4122.*;
import static java.util.Arrays.*;
import static org.mockito.Mockito.*;

public class RFC4122Version3Test extends CoreTheoryParameterTest {
    @SuppressWarnings("unchecked")
    @Override protected Class<? extends Generator>[] explicitGenerators() {
        return (Class<? extends Generator>[]) new Class<?>[] { Version3.class };
    }

    @Override protected Map<Class<? extends Annotation>, Annotation> configurations() {
        Namespace namespace = mock(Namespace.class);
        when(namespace.value()).thenReturn(Namespaces.DNS);
        return Collections.<Class<? extends Annotation>, Annotation> singletonMap(Namespace.class, namespace);
    }

    @Override protected void primeSourceOfRandomness() throws Exception {
        when(Generating.charsForString(randomForParameterGenerator))
            .thenReturn(0x61).thenReturn(0x62).thenReturn(0x63).thenReturn(0x64).thenReturn(0x65).thenReturn(0x66);
    }

    @Override protected Type parameterType() {
        return UUID.class;
    }

    @Override protected int sampleSize() {
        return 4;
    }

    @Override protected List<?> randomValues() {
        return asList(UUID.fromString("c87ee674-4ddc-3efe-a74e-dfe25da5d7b3"),
            UUID.fromString("4c104dd0-4821-30d5-9ce3-0e7a1f8b7c0d"),
            UUID.fromString("64e53c89-a376-3b78-b06b-659a3cb12c7e"),
            UUID.fromString("6328f051-6873-3241-b344-350089a65da0"));
    }

    @Override public void verifyInteractionWithRandomness() {
        verifyCharsForString(randomForParameterGenerator, times(6));
    }
}

package com.pholser.junit.quickcheck;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.pholser.junit.quickcheck.internal.GeometricDistribution;
import com.pholser.junit.quickcheck.internal.ParameterTypeContext;
import com.pholser.junit.quickcheck.internal.PropertyParameterContext;
import com.pholser.junit.quickcheck.internal.ReflectionException;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.internal.generator.PropertyParameterGenerationContext;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.rules.ExternalResource;
import ru.vyarus.java.generics.resolver.GenericsResolver;

import static com.pholser.junit.quickcheck.internal.Reflection.*;
import static java.lang.reflect.Modifier.*;
import static java.util.stream.Collectors.*;

public abstract class PropertyRule extends ExternalResource {
    private final Method primer;
    private final GeometricDistribution distro;

    private GeneratorRepository repo;

    protected PropertyRule() {
        primer = primer();
        distro = new GeometricDistribution();
    }

    public final void provide(GeneratorRepository repo) {
        this.repo = repo;
    }

    @Override protected final void before() throws Throwable {
        Map<String, Type> typeVariables =
            GenericsResolver.resolve(PropertyRule.this.getClass())
            .method(primer)
            .genericsMap();

        List<PropertyParameterGenerationContext> parameters =
            Arrays.stream(primer.getParameters())
                .map(p -> parameterContextFor(p, typeVariables))
                .map(p -> new PropertyParameterGenerationContext(
                    p,
                    repo,
                    distro,
                    new SourceOfRandomness(new Random())
                ))
                .collect(toList());

        List<Object> arguments =
            parameters.stream()
                .map(PropertyParameterGenerationContext::generate)
                .collect(toList());

        primer.invoke(PropertyRule.this, arguments.toArray());
    }

    private Method primer() {
        return Arrays.stream(PropertyRule.this.getClass().getDeclaredMethods())
            .filter(m ->
                "prime".equals(m.getName())
                    && isPublic(m.getModifiers())
                    && !isStatic(m.getModifiers())
                    && !isAbstract(m.getModifiers())
                    && void.class.equals(m.getReturnType()))
            .findFirst()
            .orElseThrow(() ->
                new ReflectionException(
                    "No public void method named 'prime' on "
                        + PropertyRule.this.getClass()));
    }

    private PropertyParameterContext parameterContextFor(
        Parameter parameter,
        Map<String, Type> typeVariables) {

        return new PropertyParameterContext(
            new ParameterTypeContext(
                parameter.getName(),
                parameter.getAnnotatedType(),
                declarerName(parameter),
                typeVariables)
                .allowMixedTypes(true),
            100
        ).annotate(parameter);
    }
}

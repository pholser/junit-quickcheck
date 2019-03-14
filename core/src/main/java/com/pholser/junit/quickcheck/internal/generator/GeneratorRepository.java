/*
 The MIT License

 Copyright (c) 2010-2018 Paul R. Holser, Jr.

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.pholser.junit.quickcheck.internal.generator;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.pholser.junit.quickcheck.generator.Ctor;
import com.pholser.junit.quickcheck.generator.Fields;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.Generators;
import com.pholser.junit.quickcheck.internal.ParameterTypeContext;
import com.pholser.junit.quickcheck.internal.Weighted;
import com.pholser.junit.quickcheck.internal.Zilch;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.javaruntype.type.TypeParameter;
import org.javaruntype.type.Types;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

import static com.pholser.junit.quickcheck.internal.Items.*;
import static com.pholser.junit.quickcheck.internal.Reflection.*;

public class GeneratorRepository implements Generators {
    private static final Set<String> NULLABLE_ANNOTATIONS = unmodifiableSet(Stream.of(
            "javax.annotation.Nullable", // JSR-305
            NullAllowed.class.getCanonicalName()
    ).collect(toSet()));

    private final SourceOfRandomness random;
    private final Map<Class<?>, Set<Generator<?>>> generators;

    public GeneratorRepository(SourceOfRandomness random) {
        this(random, new HashMap<>());
    }

    private GeneratorRepository(SourceOfRandomness random, Map<Class<?>, Set<Generator<?>>> generators) {
        this.random = random;
        this.generators = generators;
    }

    public GeneratorRepository register(Generator<?> source) {
        registerTypes(source);
        return this;
    }

    public GeneratorRepository register(Iterable<Generator<?>> source) {
        for (Generator<?> each : source)
            registerTypes(each);

        return this;
    }

    private void registerTypes(Generator<?> generator) {
        for (Class<?> each : generator.types())
            registerHierarchy(each, generator);
    }

    private void registerHierarchy(Class<?> type, Generator<?> generator) {
        maybeRegisterGeneratorForType(type, generator);

        if (type.getSuperclass() != null)
            registerHierarchy(type.getSuperclass(), generator);
        else if (type.isInterface())
            registerHierarchy(Object.class, generator);

        for (Class<?> each : type.getInterfaces())
            registerHierarchy(each, generator);
    }

    private void maybeRegisterGeneratorForType(
        Class<?> type,
        Generator<?> generator) {

        if (generator.canRegisterAsType(type))
            registerGeneratorForType(type, generator);
    }

    private void registerGeneratorForType(
        Class<?> type,
        Generator<?> generator) {

        Set<Generator<?>> forType =
            generators.computeIfAbsent(type, k -> new LinkedHashSet<>());

        forType.add(generator);
    }

    @Override public Generator<?> field(Class<?> type, String fieldName) {
        return field(findField(type, fieldName));
    }

    @Override public <U> Generator<U> constructor(
        Class<U> type,
        Class<?>... argumentTypes) {

        Constructor<U> constructor = findConstructor(type, argumentTypes);
        Ctor<U> ctor = new Ctor<>(constructor);
        ctor.provide(this);
        ctor.configure(constructor.getAnnotatedReturnType());

        return ctor;
    }

    @Override public <U> Generator<U> fieldsOf(Class<U> type) {
        Fields<U> fields = new Fields<>(type);

        fields.provide(this);
        fields.configure(type);

        return fields;
    }

    @SuppressWarnings("unchecked")
    @Override public <T> Generator<T> type(Class<T> type, Class<?>... componentTypes) {
        Generator<T> generator =
            (Generator<T>) produceGenerator(new ParameterTypeContext(type));
        generator.addComponentGenerators(
            Arrays.stream(componentTypes).map(c -> type(c)).collect(toList()));
        return generator;
    }

    @Override public Generator<?> parameter(Parameter parameter) {
        return produceGenerator(
            new ParameterTypeContext(
                parameter.getName(),
                parameter.getAnnotatedType(),
                parameter.getDeclaringExecutable().getName()
            ).annotate(parameter));
    }

    @Override public Generator<?> field(Field field) {
        return produceGenerator(
            new ParameterTypeContext(
                field.getName(),
                field.getAnnotatedType(),
                field.getDeclaringClass().getName()
            ).annotate(field));
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    @Override public final <T> Generator<T> oneOf(
        Class<? extends T> first,
        Class<? extends T>... rest) {

        return oneOf(
            type(first),
            Arrays.stream(rest)
                .map(t -> type(t))
                .toArray(Generator[]::new));
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    @Override public final <T> Generator<T> oneOf(
        Generator<? extends T> first,
        Generator<? extends T>... rest) {

        List<Generator<? extends T>> gens = new ArrayList<>();
        gens.add(first);
        Collections.addAll(gens, rest);

        List<Weighted<Generator<?>>> weightings = gens.stream()
            .map(g -> new Weighted<Generator<?>>(g, 1))
            .collect(toList());

        return (Generator<T>) new CompositeGenerator(weightings);
    }

    @Override public final <T extends Generator<?>> T make(
        Class<T> genType,
        Generator<?>... components) {

        T generator = instantiate(genType);
        generator.provide(this);
        generator.configure(genType);
        generator.addComponentGenerators(asList(components));

        return generator;
    }

    @Override public final Generators withRandom(SourceOfRandomness other) {
        return new GeneratorRepository(other, this.generators);
    }

    public Generator<?> produceGenerator(ParameterTypeContext parameter) {
        Generator<?> generator = generatorFor(parameter);

        if (!isPrimitiveType(parameter.annotatedType().getType()) && hasNullableAnnotation(parameter.annotatedElement()))
            generator = new NullableGenerator<>(generator);

        generator.provide(this);
        generator.configure(parameter.annotatedType());
        if (parameter.topLevel())
            generator.configure(parameter.annotatedElement());

        return generator;
    }

    public Generator<?> generatorFor(ParameterTypeContext parameter) {
        if (!parameter.explicitGenerators().isEmpty())
            return composeWeighted(parameter, parameter.explicitGenerators());
        if (parameter.isArray())
            return generatorForArrayType(parameter);
        if (parameter.isEnum())
            return new EnumGenerator(parameter.getRawClass());

        return compose(parameter, matchingGenerators(parameter));
    }

    private Generator<?> generatorForArrayType(ParameterTypeContext parameter) {
        ParameterTypeContext component = parameter.arrayComponentContext();
        return new ArrayGenerator(component.getRawClass(), generatorFor(component));
    }

    private List<Generator<?>> matchingGenerators(ParameterTypeContext parameter) {
        List<Generator<?>> matches = new ArrayList<>();

        if (!hasGeneratorsFor(parameter)) {
            maybeAddGeneratorByNamingConvention(parameter, matches);
            maybeAddLambdaGenerator(parameter, matches);
        } else {
            maybeAddGeneratorsFor(parameter, matches);
        }
        if (matches.isEmpty()) {
            throw new IllegalArgumentException(
                "Cannot find generator for " + parameter.name()
                + " of type " + parameter.type().getTypeName());
        }

        return matches;
    }

    private void maybeAddGeneratorByNamingConvention(
            ParameterTypeContext parameter,
            List<Generator<?>> matches
    ) {
        Class<?> genClass;
        try {
            genClass = Class.forName(parameter.getRawClass().getName() + "Gen");
        } catch (ClassNotFoundException e) {
            // class with naming convention doesn't exist
            return;
        }

        if (Generator.class.isAssignableFrom(genClass)) {
            try {
                Generator<?> generator = (Generator<?>) genClass.newInstance();
                if (generator.types().contains(parameter.getRawClass())) {
                    matches.add(generator);
                }
            } catch (IllegalAccessException | InstantiationException e) {
                throw new IllegalStateException("Cannot instantiate " + genClass.getName() + " using default constructor.");
            }
        }
    }

    private void maybeAddLambdaGenerator(
        ParameterTypeContext parameter,
        List<Generator<?>> matches) {

        Method method = singleAbstractMethodOf(parameter.getRawClass());
        if (method != null) {
            ParameterTypeContext returnType =
                new ParameterTypeContext(
                    "return value",
                    method.getAnnotatedReturnType(),
                    method.getName())
                    .annotate(method.getAnnotatedReturnType())
                    .allowMixedTypes(true);
            Generator<?> returnTypeGenerator = generatorFor(returnType);

            Generator<?> lambda =
                new LambdaGenerator<>(parameter.getRawClass(), returnTypeGenerator);
            matches.add(lambda);
        }
    }

    private void maybeAddGeneratorsFor(
        ParameterTypeContext parameter,
        List<Generator<?>> matches) {

        List<Generator<?>> candidates = generatorsFor(parameter);
        List<TypeParameter<?>> typeParameters = parameter.getTypeParameters();

        if (typeParameters.isEmpty())
            matches.addAll(candidates);
        else {
            for (Generator<?> each : candidates) {
                if (each.canGenerateForParametersOfTypes(typeParameters))
                    matches.add(each);
            }
        }
    }

    private Generator<?> compose(ParameterTypeContext parameter, List<Generator<?>> matches) {
        List<Weighted<Generator<?>>> weightings = matches.stream()
            .map(g -> new Weighted<Generator<?>>(g, 1))
            .collect(toList());

        return composeWeighted(parameter, weightings);
    }

    private Generator<?> composeWeighted(
        ParameterTypeContext parameter,
        List<Weighted<Generator<?>>> matches) {

        List<Generator<?>> forComponents = new ArrayList<>();
        for (ParameterTypeContext each : parameter.typeParameterContexts(random))
            forComponents.add(generatorFor(each));

        for (Weighted<Generator<?>> each : matches)
            applyComponentGenerators(each.item, forComponents);

        return new CompositeGenerator(matches);
    }

    private void applyComponentGenerators(Generator<?> generator, List<Generator<?>> componentGenerators) {
        if (generator.hasComponents()) {
            if (componentGenerators.isEmpty()) {
                List<Generator<?>> substitutes = new ArrayList<>();
                Generator<?> zilch = generatorFor(
                    new ParameterTypeContext(
                        "Zilch",
                        null,
                        getClass().getName(),
                        token(Zilch.class),
                        emptyMap())
                    .allowMixedTypes(true));
                for (int i = 0; i < generator.numberOfNeededComponents(); ++i)
                    substitutes.add(zilch);

                generator.addComponentGenerators(substitutes);
            } else
                generator.addComponentGenerators(componentGenerators);
        }
    }

    private List<Generator<?>> generatorsFor(ParameterTypeContext parameter) {
        Set<Generator<?>> matches = generators.get(parameter.getRawClass());

        if (!parameter.allowMixedTypes()) {
            Generator<?> match = choose(matches, random);
            matches = new HashSet<>();
            matches.add(match);
        }

        List<Generator<?>> copies = new ArrayList<>();
        for (Generator<?> each : matches)
            copies.add(each.copy());
        return copies;
    }

    private boolean hasGeneratorsFor(ParameterTypeContext parameter) {
        return generators.get(parameter.getRawClass()) != null;
    }

    private static org.javaruntype.type.Type<?> token(Type type) {
        return Types.forJavaLangReflectType(type);
    }

    private static boolean isPrimitiveType(Type type) {
        return type instanceof Class<?> && ((Class<?>)type).isPrimitive();
    }

    private static boolean hasNullableAnnotation(AnnotatedElement annotatedElement) {
        return annotatedElement != null &&
                Arrays.stream(annotatedElement.getAnnotations())
                        .map(Annotation::annotationType)
                        .map(Class::getCanonicalName)
                        .anyMatch(NULLABLE_ANNOTATIONS::contains);
    }
}

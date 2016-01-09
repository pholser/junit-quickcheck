/*
 The MIT License

 Copyright (c) 2010-2015 Paul R. Holser, Jr.

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

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.ParameterTypeContext;
import com.pholser.junit.quickcheck.internal.Weighted;
import com.pholser.junit.quickcheck.internal.Zilch;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.javaruntype.type.TypeParameter;
import org.javaruntype.type.Types;

import static com.pholser.junit.quickcheck.internal.Items.*;
import static com.pholser.junit.quickcheck.internal.Reflection.*;
import static java.util.Collections.*;

public class GeneratorRepository {
    private final SourceOfRandomness random;
    private final Map<Class<?>, Set<Generator<?>>> generators = new HashMap<>();

    public GeneratorRepository(SourceOfRandomness random) {
        this.random = random;
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

    private void maybeRegisterGeneratorForType(Class<?> type, Generator<?> generator) {
        if (generator.canRegisterAsType(type))
            registerGeneratorForType(type, generator);
    }

    private void registerGeneratorForType(Class<?> type, Generator<?> generator) {
        Set<Generator<?>> forType = generators.get(type);
        if (forType == null) {
            forType = new LinkedHashSet<>();
            generators.put(type, forType);
        }

        forType.add(generator);
    }

    public Generator<?> produceGenerator(ParameterTypeContext parameter) {
        Generator<?> generator = generatorFor(parameter);
        generator.provideRepository(this);
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

        if (!hasGeneratorsFor(parameter))
            maybeAddLambdaGenerator(parameter, matches);
        else
            maybeAddGeneratorsFor(parameter, matches);

        if (matches.isEmpty()) {
            throw new IllegalArgumentException(
                "Cannot find generator for " + parameter.name()
                + " of type " + parameter.type().getTypeName());
        }

        return matches;
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

            @SuppressWarnings("unchecked")
            Generator<?> lambda =
                new LambdaGenerator(parameter.getRawClass(), returnTypeGenerator);
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
            .collect(Collectors.toList());

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
            copies.add(copyOf(each));
        return copies;
    }

    private boolean hasGeneratorsFor(ParameterTypeContext parameter) {
        return generators.get(parameter.getRawClass()) != null;
    }

    private static Generator<?> copyOf(Generator<?> generator) {
        return instantiate(generator.getClass());
    }

    public static org.javaruntype.type.Type<?> token(Type type) {
        return Types.forJavaLangReflectType(type);
    }
}

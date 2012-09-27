/*
 The MIT License

 Copyright (c) 2010-2012 Paul R. Holser, Jr.

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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.pholser.junit.quickcheck.generator.ArrayGenerator;
import com.pholser.junit.quickcheck.generator.EnumGenerator;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.Items;
import com.pholser.junit.quickcheck.internal.Reflection;
import com.pholser.junit.quickcheck.generator.SourceOfRandomness;
import org.javaruntype.type.ExtendsTypeParameter;
import org.javaruntype.type.StandardTypeParameter;
import org.javaruntype.type.TypeParameter;
import org.javaruntype.type.Types;
import org.javaruntype.type.WildcardTypeParameter;

import static org.javaruntype.type.Types.*;

public class GeneratorRepository {
    private final SourceOfRandomness random;
    private final Map<Class<?>, Set<Generator<?>>> generators = new HashMap<Class<?>, Set<Generator<?>>>();

    public GeneratorRepository(SourceOfRandomness random) {
        this.random = random;
    }

    public boolean isEmpty() {
        return generators.isEmpty();
    }

    public GeneratorRepository add(Generator<?> source) {
        registerTypes(source);
        return this;
    }

    public GeneratorRepository add(Iterable<Generator<?>> source) {
        for (Generator<?> each : source)
            registerTypes(each);

        return this;
    }

    private void registerTypes(Generator<?> generator) {
        for (Class<?> each : generator.types())
            registerHierarchy(each, generator);
    }

    private void registerHierarchy(Class<?> type, Generator<?> generator) {
        addGenerator(type, generator);

        if (type.getSuperclass() != null)
            registerHierarchy(type.getSuperclass(), generator);
        for (Class<?> each : type.getInterfaces())
            registerHierarchy(each, generator);
    }

    private void addGenerator(Class<?> type, Generator<?> generator) {
        // Do not feed Collections or Maps to things of type Object, including type parameters.
        if (Object.class.equals(type)) {
            Class<?> firstType = generator.types().get(0);
            if (Collection.class.isAssignableFrom(firstType) || Map.class.isAssignableFrom(firstType))
                return;
        }

        Set<Generator<?>> forType = generators.get(type);
        if (forType == null) {
            forType = new LinkedHashSet<Generator<?>>();
            generators.put(type, forType);
        }

        forType.add(generator);
    }

    public Generator<?> generatorFor(Type type) {
        return generatorForTypeToken(Types.forJavaLangReflectType(type), true);
    }

    private Generator<?> generatorForTypeToken(org.javaruntype.type.Type<?> typeToken, boolean allowMixedTypes) {
        if (typeToken.isArray()) {
            @SuppressWarnings("unchecked")
            org.javaruntype.type.Type<Object[]> arrayTypeToken = (org.javaruntype.type.Type<Object[]>) typeToken;

            org.javaruntype.type.Type<?> component = arrayComponentOf(arrayTypeToken);
            return new ArrayGenerator(component.getRawClass(), generatorForTypeToken(component, true));
        }
        
        if (typeToken.getRawClass().isEnum())
            return new EnumGenerator(typeToken.getRawClass());

        List<Generator<?>> matches = generatorsForRawClass(typeToken.getRawClass(), allowMixedTypes);

        List<Generator<?>> forComponents = new ArrayList<Generator<?>>();
        for (TypeParameter<?> each : typeToken.getTypeParameters())
            forComponents.add(generatorForTypeParameter(each));
        for (Generator<?> each : matches)
            applyComponentGenerators(each, forComponents);

        return new CompositeGenerator(matches);
    }

    private void applyComponentGenerators(Generator<?> generator, List<Generator<?>> componentGenerators) {
        if (generator.hasComponents()) {
            if (componentGenerators.isEmpty()) {
                List<Generator<?>> substitutes = new ArrayList<Generator<?>>();
                for (int i = 0; i < generator.numberOfNeededComponents(); ++i)
                    substitutes.add(generatorFor(int.class));
                generator.addComponentGenerators(substitutes);
            } else
                generator.addComponentGenerators(componentGenerators);
        }
    }

    private Generator<?> generatorForTypeParameter(TypeParameter<?> parameter) {
        if (parameter instanceof StandardTypeParameter<?>)
            return generatorForTypeToken(parameter.getType(), true);
        if (parameter instanceof WildcardTypeParameter)
            return generatorFor(int.class);
        if (parameter instanceof ExtendsTypeParameter<?>)
            return generatorForTypeToken(parameter.getType(), false);

        // must be "? super X"
        Set<org.javaruntype.type.Type<?>> supertypes = Reflection.supertypes(parameter.getType());
        org.javaruntype.type.Type<?> chosen = Items.randomElementFrom(supertypes, random);
        return generatorForTypeToken(chosen, false);
    }

    private List<Generator<?>> generatorsForRawClass(Class<?> clazz, boolean allowMixedTypes) {
        Set<Generator<?>> matches = generators.get(clazz);
        if (matches == null)
            throw new IllegalArgumentException("Cannot find generator for " + clazz);

        if (!allowMixedTypes) {
            Generator<?> singleMatch = Items.randomElementFrom(matches, random);
            matches = new HashSet<Generator<?>>();
            matches.add(singleMatch);
        }

        List<Generator<?>> copies = new ArrayList<Generator<?>>();
        for (Generator<?> each : matches)
            copies.add(each.hasComponents() ? copyOf(each) : each);
        return copies;
    }

    private static Generator<?> copyOf(Generator<?> generator) {
        return Reflection.instantiate(generator.getClass());
    }
}

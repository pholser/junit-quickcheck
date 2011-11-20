/*
 The MIT License

 Copyright (c) 2010-2011 Paul R. Holser, Jr.

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

/*
 The MIT License

 Copyright (c) 2010-2011 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.internal.extractors;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.pholser.junit.quickcheck.RandomValueExtractor;
import com.pholser.junit.quickcheck.internal.Reflection;
import org.javaruntype.type.TypeParameter;
import org.javaruntype.type.Types;
import org.javaruntype.type.WildcardTypeParameter;

import static org.javaruntype.type.Types.*;

public class ExtractorRepository {
    private final Map<Class<?>, Set<RandomValueExtractor<?>>> extractors =
        new HashMap<Class<?>, Set<RandomValueExtractor<?>>>();

    public ExtractorRepository add(Iterable<RandomValueExtractor<?>> source) {
        for (RandomValueExtractor<?> each : source)
            registerTypes(each);

        return this;
    }

    private void registerTypes(RandomValueExtractor<?> extractor) {
        for (Class<?> each : extractor.types())
            registerHierarchy(each, extractor);
    }

    private void registerHierarchy(Class<?> type, RandomValueExtractor<?> extractor) {
        addExtractor(type, extractor);
        if (type.getSuperclass() != null)
            registerHierarchy(type.getSuperclass(), extractor);
        for (Class<?> each : type.getInterfaces())
            registerHierarchy(each, extractor);
    }

    private void addExtractor(Class<?> type, RandomValueExtractor<?> extractor) {
        // Do not feed Collections or Maps to things of type Object, including type parameters.
        if (Object.class.equals(type)) {
            Class<?> firstType = extractor.types().get(0);
            if (Collection.class.isAssignableFrom(firstType) || Map.class.isAssignableFrom(firstType))
                return;
        }

        Set<RandomValueExtractor<?>> forType = extractors.get(type);
        if (forType == null) {
            forType = new LinkedHashSet<RandomValueExtractor<?>>();
            extractors.put(type, forType);
        }

        forType.add(extractor);
    }

    public RandomValueExtractor<?> extractorFor(Type type) {
        return extractorForTypeToken(Types.forJavaLangReflectType(type));
    }

    private RandomValueExtractor<?> extractorForTypeToken(org.javaruntype.type.Type<?> typeToken) {
        if (typeToken.isArray()) {
            @SuppressWarnings("unchecked")
            org.javaruntype.type.Type<Object[]> arrayTypeToken = (org.javaruntype.type.Type<Object[]>) typeToken;

            org.javaruntype.type.Type<?> component = arrayComponentOf(arrayTypeToken);
            return new ArrayExtractor(component.getRawClass(), extractorForTypeToken(component));
        }

        List<RandomValueExtractor<?>> matches = extractorsForRawClass(typeToken.getRawClass());

        List<RandomValueExtractor<?>> forComponents = new ArrayList<RandomValueExtractor<?>>();
        for (TypeParameter<?> each : typeToken.getTypeParameters())
            forComponents.add(extractorForTypeParameter(each));

        for (RandomValueExtractor<?> each : matches) {
            if (each.hasComponents())
                each.addComponentExtractors(forComponents);
        }

        return new CompositeRandomValueExtractor(matches);
    }

    private RandomValueExtractor<?> extractorForTypeParameter(TypeParameter<?> parameter) {
        if (parameter instanceof WildcardTypeParameter)
            return extractorFor(Object.class);

        return extractorForTypeToken(parameter.getType());
    }

    private List<RandomValueExtractor<?>> extractorsForRawClass(Class<?> clazz) {
        Set<RandomValueExtractor<?>> matches = extractors.get(clazz);
        if (matches == null)
            throw new IllegalArgumentException("Cannot find extractor for " + clazz);

        List<RandomValueExtractor<?>> copies = new ArrayList<RandomValueExtractor<?>>();
        for (RandomValueExtractor<?> each : matches)
            copies.add(each.hasComponents() ? copyOf(each) : each);
        return copies;
    }

    private static RandomValueExtractor<?> copyOf(RandomValueExtractor<?> extractor) {
        return Reflection.instantiate(extractor.getClass());
    }
}

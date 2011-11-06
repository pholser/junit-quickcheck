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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pholser.junit.quickcheck.RandomValueExtractor;
import com.pholser.junit.quickcheck.RegisterableRandomValueExtractor;
import org.javaruntype.type.Types;

import static org.javaruntype.type.Types.*;

public class ExtractorRepository {
    private final Map<Class<?>, List<RandomValueExtractor<?>>> extractors =
        new HashMap<Class<?>, List<RandomValueExtractor<?>>>();

    public ExtractorRepository add(Iterable<RegisterableRandomValueExtractor<?>> source) {
        for (RegisterableRandomValueExtractor<?> each : source)
            registerTypes(each);

        return this;
    }

    private void registerTypes(RegisterableRandomValueExtractor<?> extractor) {
        for (Class<?> each : extractor.types())
            registerHierarchy(each, extractor);
    }

    private void registerHierarchy(Class<?> type, RegisterableRandomValueExtractor<?> extractor) {
        addExtractor(type, extractor);
        if (type.getSuperclass() != null)
            registerHierarchy(type.getSuperclass(), extractor);
        for (Class<?> each : type.getInterfaces())
            registerHierarchy(each, extractor);
    }

    private void addExtractor(Class<?> type, RegisterableRandomValueExtractor<?> extractor) {
        List<RandomValueExtractor<?>> typeExtractors = extractors.get(type);
        if (typeExtractors == null) {
            typeExtractors = new ArrayList<RandomValueExtractor<?>>();
            extractors.put(type, typeExtractors);
        }

        typeExtractors.add(extractor);
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

        if (List.class.equals(typeToken.getRawClass())) {
            Class<?> componentType = typeToken.getTypeParameters().get(0).getType().getRawClass();
            return new ListExtractor(extractorFor(componentType));
        }

        List<RandomValueExtractor<?>> matches = extractors.get(typeToken.getRawClass());
        if (matches == null)
            throw new IllegalArgumentException("Cannot find extractor for " + typeToken.getRawClass());

        return new CompositeRandomValueExtractor(matches);
    }
}

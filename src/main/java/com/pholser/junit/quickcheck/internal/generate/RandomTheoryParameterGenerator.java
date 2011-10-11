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

package com.pholser.junit.quickcheck.internal.generate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.RandomValueExtractor;
import com.pholser.junit.quickcheck.RegisterableRandomValueExtractor;
import com.pholser.junit.quickcheck.internal.extractors.ArrayExtractor;
import com.pholser.junit.quickcheck.internal.extractors.ListExtractor;
import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;
import org.javaruntype.type.Types;
import org.junit.experimental.theories.PotentialAssignment;

public class RandomTheoryParameterGenerator implements TheoryParameterGenerator {
    private final SourceOfRandomness random;
    private final Map<Type, RandomValueExtractor<?>> extractors =
        new HashMap<Type, RandomValueExtractor<?>>();

    public RandomTheoryParameterGenerator(SourceOfRandomness random,
        Iterable<RegisterableRandomValueExtractor>... extractorSources) {

        this.random = random;
        for (Iterable<RegisterableRandomValueExtractor> each : extractorSources)
            register(each);
    }

    private void register(Iterable<RegisterableRandomValueExtractor> source) {
        for (RegisterableRandomValueExtractor<?> each : source)
            registerTypes(each);
    }

    private void registerTypes(RegisterableRandomValueExtractor<?> extractor) {
        for (Class<?> each : extractor.types())
            extractors.put(each, extractor);
    }

    @Override
    public List<PotentialAssignment> generate(ForAll quantifier, Type type) {
        List<PotentialAssignment> assignments = new ArrayList<PotentialAssignment>();
        RandomValueExtractor<?> extractor = extractorFor(type);

        for (int i = 0, sampleSize = quantifier.sampleSize(); i < sampleSize; ++i) {
            Object nextValue = extractor.extract(random);
            assignments.add(PotentialAssignment.forValue(String.valueOf(nextValue), nextValue));
        }

        return assignments;
    }

    private RandomValueExtractor<?> extractorFor(Type type) {
        org.javaruntype.type.Type<?> typeToken = Types.forJavaLangReflectType(type);

        if (typeToken.isArray()) {
            Class<?> componentType = typeToken.getComponentClass();
            return new ArrayExtractor(componentType, extractors.get(componentType));
        } else if (List.class.equals(typeToken.getRawClass())) {
            Class<?> componentType = typeToken.getTypeParameters().get(0).getType().getRawClass();
            return new ListExtractor(extractors.get(componentType));
        }

        return extractors.get(type);
    }
}

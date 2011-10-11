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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pholser.junit.quickcheck.internal.extractors.ArrayExtractor;
import com.pholser.junit.quickcheck.internal.extractors.BigDecimalExtractor;
import com.pholser.junit.quickcheck.internal.extractors.BigIntegerExtractor;
import com.pholser.junit.quickcheck.internal.extractors.BooleanExtractor;
import com.pholser.junit.quickcheck.internal.extractors.ByteExtractor;
import com.pholser.junit.quickcheck.internal.extractors.CharExtractor;
import com.pholser.junit.quickcheck.internal.extractors.DoubleExtractor;
import com.pholser.junit.quickcheck.internal.extractors.FloatExtractor;
import com.pholser.junit.quickcheck.internal.extractors.IntegerExtractor;
import com.pholser.junit.quickcheck.internal.extractors.ListExtractor;
import com.pholser.junit.quickcheck.internal.extractors.LongExtractor;
import com.pholser.junit.quickcheck.internal.extractors.RandomValueExtractor;
import com.pholser.junit.quickcheck.internal.extractors.ShortExtractor;
import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;
import org.javaruntype.type.Types;

import com.pholser.junit.quickcheck.ForAll;
import org.junit.experimental.theories.PotentialAssignment;

public class RandomTheoryParameterGenerator implements TheoryParameterGenerator {
    private final SourceOfRandomness random;
    private final Map<Class<?>, RandomValueExtractor<?>> extractors =
        new HashMap<Class<?>, RandomValueExtractor<?>>();

    public RandomTheoryParameterGenerator(SourceOfRandomness random) {
        this.random = random;
        extractors.put(int.class, new IntegerExtractor());
        extractors.put(Integer.class, new IntegerExtractor());
        extractors.put(short.class, new ShortExtractor());
        extractors.put(Short.class, new ShortExtractor());
        extractors.put(char.class, new CharExtractor());
        extractors.put(Character.class, new CharExtractor());
        extractors.put(byte.class, new ByteExtractor());
        extractors.put(Byte.class, new ByteExtractor());
        extractors.put(boolean.class, new BooleanExtractor());
        extractors.put(Boolean.class, new BooleanExtractor());
        extractors.put(long.class, new LongExtractor());
        extractors.put(Long.class, new LongExtractor());
        extractors.put(float.class, new FloatExtractor());
        extractors.put(Float.class, new FloatExtractor());
        extractors.put(double.class, new DoubleExtractor());
        extractors.put(Double.class, new DoubleExtractor());
        extractors.put(BigInteger.class, new BigIntegerExtractor());
        extractors.put(BigDecimal.class, new BigDecimalExtractor());
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

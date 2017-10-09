/*
 The MIT License

 Copyright (c) 2010-2016 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.runner.sampling;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.pholser.junit.quickcheck.generator.Also;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.Only;
import com.pholser.junit.quickcheck.internal.ParameterSampler;
import com.pholser.junit.quickcheck.internal.ParameterTypeContext;
import com.pholser.junit.quickcheck.internal.SeededValue;
import com.pholser.junit.quickcheck.conversion.StringConversion;
import com.pholser.junit.quickcheck.internal.conversion.StringConversions;
import com.pholser.junit.quickcheck.internal.generator.ExhaustiveDomainGenerator;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.internal.generator.GuaranteeValuesGenerator;
import com.pholser.junit.quickcheck.internal.generator.PropertyParameterGenerationContext;
import com.pholser.junit.quickcheck.internal.generator.SamplingDomainGenerator;

import static java.util.stream.Collectors.*;

public class TupleParameterSampler implements ParameterSampler {
    private final int trials;

    public TupleParameterSampler(int trials) {
        this.trials = trials;
    }

    @Override public int sizeFactor(ParameterTypeContext p) {
        return trials;
    }

    @Override public Stream<List<SeededValue>> sample(
        List<PropertyParameterGenerationContext> parameters) {

        Stream<List<SeededValue>> tupleStream =
            Stream.generate(
                () -> parameters.stream()
                    .map(SeededValue::new)
                    .collect(toList()));
        return tupleStream.limit(trials);
    }

    @Override public Generator<?> decideGenerator(
        GeneratorRepository repository,
        ParameterTypeContext p) {

        Only only = p.annotatedType().getAnnotation(Only.class);
        if (only != null) {
            StringConversion conversion = StringConversions.decide(p, only);
            Set<Object> values =
                Arrays.stream(only.value())
                    .map(conversion::convert)
                    .collect(toSet());
            return new SamplingDomainGenerator(values);
        }

        Also also = p.annotatedType().getAnnotation(Also.class);
        if (also != null) {
            StringConversion conversion = StringConversions.decide(p, also);
            Set<Object> values =
                Arrays.stream(also.value())
                    .map(conversion::convert)
                    .collect(toSet());
            return new GuaranteeValuesGenerator(
                new ExhaustiveDomainGenerator(values),
                repository.produceGenerator(p));
        }

        return repository.produceGenerator(p);
    }
}

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

package com.pholser.junit.quickcheck.internal;

import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.internal.generator.RandomTheoryParameterGenerator;
import com.pholser.junit.quickcheck.internal.generator.ServiceLoaderGeneratorSource;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.contrib.theories.ParameterSignature;
import org.junit.contrib.theories.ParameterSupplier;
import org.junit.contrib.theories.PotentialAssignment;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

@Deprecated
public class RandomValueSupplier extends ParameterSupplier {
    private final RandomTheoryParameterGenerator generator;

    /* Called by JUnit reflectively. */
    public RandomValueSupplier() {
        SourceOfRandomness random = new SourceOfRandomness(new Random());
        generator = new RandomTheoryParameterGenerator(
            random,
            new GeneratorRepository(random)
                .register(new ServiceLoaderGeneratorSource()),
            new GeometricDistribution(),
            LoggerFactory.getLogger("junit-quickcheck.seed-reporting"));
    }

    @Override public List<PotentialAssignment> getValueSources(ParameterSignature signature) {
        ParameterTypeContext typeContext =
            new ParameterTypeContext(
                signature.getName(),
                signature.getAnnotatedType(),
                signature.getDeclarerName());
        ParameterContext parameter = new ParameterContext(typeContext).annotate(signature);
        return generator.generate(parameter);
    }
}

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

package com.pholser.junit.quickcheck.internal;

import java.util.List;

import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParameterSupplier;
import org.junit.experimental.theories.PotentialAssignment;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.internal.extractors.BasicExtractorSource;
import com.pholser.junit.quickcheck.internal.extractors.ExtractorRepository;
import com.pholser.junit.quickcheck.internal.extractors.ServiceLoaderExtractorSource;
import com.pholser.junit.quickcheck.internal.generate.RandomTheoryParameterGenerator;
import com.pholser.junit.quickcheck.internal.generate.TheoryParameterGenerator;
import com.pholser.junit.quickcheck.internal.random.JDKSourceOfRandomness;

public class RandomValueSupplier extends ParameterSupplier {
    private final TheoryParameterGenerator generator;

    public RandomValueSupplier() {
        this(new RandomTheoryParameterGenerator(new JDKSourceOfRandomness(),
            new ExtractorRepository().add(new ServiceLoaderExtractorSource()).add(new BasicExtractorSource())));
    }

    protected RandomValueSupplier(TheoryParameterGenerator generator) {
        this.generator = generator;
    }

    @Override
    public List<PotentialAssignment> getValueSources(ParameterSignature signature) {
        return generator.generate(signature.getAnnotation(ForAll.class), signature.getType());
    }
}

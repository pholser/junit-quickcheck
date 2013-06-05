/*
 The MIT License

 Copyright (c) 2010-2013 Paul R. Holser, Jr.

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

import java.util.ArrayList;
import java.util.List;

import com.pholser.junit.quickcheck.internal.ParameterContext;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.experimental.theories.PotentialAssignment;

public class RandomTheoryParameterGenerator implements TheoryParameterGenerator {
    private final SourceOfRandomness random;
    private final GeneratorRepository repository;

    public RandomTheoryParameterGenerator(SourceOfRandomness random, GeneratorRepository repository) {
        this.random = random;
        this.repository = repository;
    }

    @Override public List<PotentialAssignment> generate(ParameterContext parameter) {
        List<PotentialAssignment> assignments = new ArrayList<PotentialAssignment>();

        for (GenerationContext gen = new GenerationContext(parameter, repository); gen.shouldContinue();) {
            Object nextValue = gen.generate(random);
            assignments.add(PotentialAssignment.forValue(String.valueOf(nextValue), nextValue));
        }

        return assignments;
    }
}

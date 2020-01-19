/*
 The MIT License

 Copyright (c) 2010-2020 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.examples.func;

import java.util.List;

import com.pholser.junit.quickcheck.generator.ComponentizedGenerator;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static com.pholser.junit.quickcheck.examples.func.Either.*;
import static java.util.stream.Collectors.*;

public class EitherGenerator extends ComponentizedGenerator<Either> {
    public EitherGenerator() {
        super(Either.class);
    }

    @Override public Either<?, ?> generate(
        SourceOfRandomness random,
        GenerationStatus status) {

        return random.nextBoolean()
            ? makeLeft(componentGenerators().get(0).generate(random, status))
            : makeRight(componentGenerators().get(1).generate(random, status));
    }

    @Override public List<Either> doShrink(
        SourceOfRandomness random,
        Either larger) {

        @SuppressWarnings("unchecked")
        Either<Object, Object> either = (Either<Object, Object>) larger;

        return either.map(
            left -> componentGenerators().get(0).shrink(random, left)
                .stream()
                .map(Either::makeLeft)
                .collect(toList()),
            right -> componentGenerators().get(1).shrink(random, right)
                .stream()
                .map(Either::makeRight)
                .collect(toList()));
    }

    @Override public int numberOfNeededComponents() {
        return 2;
    }
}

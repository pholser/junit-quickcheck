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

package com.pholser.junit.quickcheck.generator.java.lang;

import java.util.List;
import java.util.stream.Collectors;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.util.Arrays.*;

import static com.pholser.junit.quickcheck.internal.Reflection.*;

/**
 * Produces values of type {@code char} or {@link Character}.
 */
public class CharacterGenerator extends Generator<Character> {
    private char min = (Character) defaultValueOf(InRange.class, "minChar");
    private char max = (Character) defaultValueOf(InRange.class, "maxChar");

    @SuppressWarnings("unchecked") public CharacterGenerator() {
        super(asList(Character.class, char.class));
    }

    /**
     * Tells this generator to produce values within a specified minimum and/or
     * maximum, inclusive, with uniform distribution.
     *
     * {@link InRange#min} and {@link InRange#max} take precedence over
     * {@link InRange#minChar()} and {@link InRange#maxChar()}, if non-empty.
     *
     * @param range annotation that gives the range's constraints
     */
    public void configure(InRange range) {
        min = range.min().isEmpty() ? range.minChar() : range.min().charAt(0);
        max = range.max().isEmpty() ? range.maxChar() : range.max().charAt(0);
    }

    @Override public Character generate(SourceOfRandomness random, GenerationStatus status) {
        return random.nextChar(min, max);
    }

    @Override public List<Character> doShrink(SourceOfRandomness random, Character larger) {
        return new CodePointShrink(cp -> cp >= min && cp <= max)
            .shrink(random, (int) larger)
            .stream()
            .map((Integer cp) -> (char) cp.intValue())
            .collect(Collectors.toList());
    }
}

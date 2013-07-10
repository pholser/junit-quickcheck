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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.pholser.junit.quickcheck.generator.java.util.ArrayListGenerator;
import com.pholser.junit.quickcheck.generator.java.math.BigDecimalGenerator;
import com.pholser.junit.quickcheck.generator.java.math.BigIntegerGenerator;
import com.pholser.junit.quickcheck.generator.java.lang.BooleanGenerator;
import com.pholser.junit.quickcheck.generator.java.lang.ByteGenerator;
import com.pholser.junit.quickcheck.generator.java.util.concurrent.CallableGenerator;
import com.pholser.junit.quickcheck.generator.java.lang.CharacterGenerator;
import com.pholser.junit.quickcheck.generator.java.util.DateGenerator;
import com.pholser.junit.quickcheck.generator.java.lang.DoubleGenerator;
import com.pholser.junit.quickcheck.generator.java.lang.FloatGenerator;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.java.util.HashMapGenerator;
import com.pholser.junit.quickcheck.generator.java.util.HashSetGenerator;
import com.pholser.junit.quickcheck.generator.java.lang.IntegerGenerator;
import com.pholser.junit.quickcheck.generator.java.lang.LongGenerator;
import com.pholser.junit.quickcheck.generator.java.lang.ShortGenerator;
import com.pholser.junit.quickcheck.generator.java.lang.StringGenerator;

public class BasicGeneratorSource implements Iterable<Generator<?>> {
    @Override public Iterator<Generator<?>> iterator() {
        List<Generator<?>> generators = Arrays.<Generator<?>> asList(
            new BigDecimalGenerator(),
            new BigIntegerGenerator(),
            new BooleanGenerator(),
            new ByteGenerator(),
            new CharacterGenerator(),
            new DoubleGenerator(),
            new FloatGenerator(),
            new IntegerGenerator(),
            new LongGenerator(),
            new ShortGenerator(),
            new VoidGenerator(),
            new CallableGenerator(),
            new StringGenerator(),
            new ArrayListGenerator(),
            new HashSetGenerator(),
            new HashMapGenerator(),
            new DateGenerator());

        return generators.iterator();
    }
}

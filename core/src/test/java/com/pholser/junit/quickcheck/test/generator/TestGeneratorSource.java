package com.pholser.junit.quickcheck.test.generator;

import com.pholser.junit.quickcheck.generator.Generator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TestGeneratorSource implements Iterable<Generator<?>> {
    @Override public Iterator<Generator<?>> iterator() {
        List<Generator<?>> generators = Arrays.<Generator<?>> asList(
                new BoxGenerator(),
                new TestArrayListGenerator(),
                new TestBigDecimalGenerator(),
                new TestBigIntegerGenerator(),
                new TestBooleanGenerator(),
                new TestByteGenerator(),
                new TestCallableGenerator(),
                new TestCharacterGenerator(),
                new TestDoubleGenerator(),
                new TestFloatGenerator(),
                new TestHashMapGenerator(),
                new TestIntegerGenerator(),
                new TestLongGenerator(),
                new TestShortGenerator(),
                new TestStringGenerator());

        return generators.iterator();
    }
}

package com.pholser.junit.quickcheck.internal.extractors;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.pholser.junit.quickcheck.RegisterableRandomValueExtractor;

public class BasicExtractorSource implements Iterable<RegisterableRandomValueExtractor<?>> {
	@Override
	public Iterator<RegisterableRandomValueExtractor<?>> iterator() {
		List<RegisterableRandomValueExtractor<?>> extractors = Arrays.<RegisterableRandomValueExtractor<?>> asList(
            new BigDecimalExtractor(),
            new BigIntegerExtractor(),
            new BooleanExtractor(),
            new ByteExtractor(),
            new CharacterExtractor(),
            new DoubleExtractor(),
            new FloatExtractor(),
            new IntegerExtractor(),
            new LongExtractor(),
            new ShortExtractor());
		return extractors.iterator();
	}
}

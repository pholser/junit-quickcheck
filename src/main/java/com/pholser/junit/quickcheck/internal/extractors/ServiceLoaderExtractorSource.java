package com.pholser.junit.quickcheck.internal.extractors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import com.pholser.junit.quickcheck.RegisterableRandomValueExtractor;

public class ServiceLoaderExtractorSource implements Iterable<RegisterableRandomValueExtractor<?>> {
	@SuppressWarnings("rawtypes")
	private final ServiceLoader<RegisterableRandomValueExtractor> loader;

	public ServiceLoaderExtractorSource() {
		this(ServiceLoader.load(RegisterableRandomValueExtractor.class));
	}

	@SuppressWarnings("rawtypes")
	protected ServiceLoaderExtractorSource(ServiceLoader<RegisterableRandomValueExtractor> loader) {
    	this.loader = loader;
	}

	@Override
	public Iterator<RegisterableRandomValueExtractor<?>> iterator() {
		List<RegisterableRandomValueExtractor<?>> extractors = new ArrayList<RegisterableRandomValueExtractor<?>>();
		for (RegisterableRandomValueExtractor<?> each : loader)
			extractors.add(each);
        return Collections.unmodifiableList(extractors).iterator();
	}
}

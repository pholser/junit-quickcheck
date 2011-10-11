package com.pholser.junit.quickcheck;

import java.util.List;

import static java.util.Arrays.*;
import static java.util.Collections.*;

public abstract class RegisterableRandomValueExtractor<T> implements RandomValueExtractor<T> {
    private final List<Class<T>> types;

    @SuppressWarnings("unchecked")
    protected RegisterableRandomValueExtractor(Class<T> type) {
        this(asList(type));
    }

    protected RegisterableRandomValueExtractor(List<Class<T>> types) {
        this.types = types;
    }

    public List<Class<T>> types() {
        return unmodifiableList(types);
    }
}

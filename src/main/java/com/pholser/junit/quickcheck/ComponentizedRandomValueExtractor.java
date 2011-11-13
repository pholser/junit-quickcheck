package com.pholser.junit.quickcheck;

import java.util.ArrayList;
import java.util.List;

public abstract class ComponentizedRandomValueExtractor<T> extends RandomValueExtractor<T> {
    protected final List<RandomValueExtractor<?>> componentExtractors = new ArrayList<RandomValueExtractor<?>>();

    protected ComponentizedRandomValueExtractor(Class<T> type) {
        super(type);
    }

    @Override
    public boolean hasComponents() {
        return true;
    }

    @Override
    public void addComponentExtractors(List<RandomValueExtractor<?>> componentExtractors) {
        this.componentExtractors.clear();
        this.componentExtractors.addAll(componentExtractors);
    }
}

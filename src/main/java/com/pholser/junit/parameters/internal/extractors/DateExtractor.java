package com.pholser.junit.parameters.internal.extractors;

import java.util.Date;

import com.pholser.junit.parameters.Between;

import com.pholser.junit.parameters.extractors.RandomValueExtractor;
import com.pholser.junit.parameters.random.SourceOfRandomness;

public class DateExtractor implements RandomValueExtractor<Date> {
    @Override
    public Date randomValue(SourceOfRandomness random) {
        return new Date(random.nextLong());
    }

    @Override
    public boolean supportsRange() {
        return false;
    }

    @Override
    public void applyRange(Between range) {
        throw new UnsupportedOperationException();
    }
}

package com.pholser.junit.quickcheck.internal;

import com.pholser.junit.quickcheck.generator.ValuesOf;
import org.junit.Test;

import java.lang.annotation.Annotation;

import static org.junit.Assert.*;

public class SampleSizerTest {
    enum Ternary {
        YES, NO, MAYBE
    }

    @Test public void primitiveBooleanWithValuesOfTrumpsSampleSize() {
        ParameterContext parameter = new ParameterContext(boolean.class);
        addValuesOf(parameter);

        SampleSizer sizer = new SampleSizer(5, parameter);

        assertEquals(2, sizer.sampleSize());
    }

    @Test public void wrapperBooleanWithValuesOfTrumpsSampleSize() {
        ParameterContext parameter = new ParameterContext(Boolean.class);
        addValuesOf(parameter);

        SampleSizer sizer = new SampleSizer(6, parameter);

        assertEquals(2, sizer.sampleSize());
    }

    @Test public void enumWithValuesOfTrumpsSampleSize() {
        ParameterContext parameter = new ParameterContext(Ternary.class);
        addValuesOf(parameter);

        SampleSizer sizer = new SampleSizer(7, parameter);

        assertEquals(3, sizer.sampleSize());
    }

    @Test public void otherClassesUseConfiguredSampleSize() {
        ParameterContext parameter = new ParameterContext(String.class);
        addValuesOf(parameter);

        SampleSizer sizer = new SampleSizer(8, parameter);

        assertEquals(8, sizer.sampleSize());
    }

    private void addValuesOf(ParameterContext parameter) {
        parameter.addConfiguration(ValuesOf.class, new ValuesOf() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return ValuesOf.class;
            }
        });
    }
}

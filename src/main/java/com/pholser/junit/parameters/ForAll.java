package com.pholser.junit.parameters;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import com.pholser.junit.parameters.internal.RandomValueSupplier;

import org.junit.experimental.theories.ParametersSuppliedBy;

@Target(PARAMETER)
@Retention(RUNTIME)
@ParametersSuppliedBy(RandomValueSupplier.class)
public @interface ForAll {
    int sampleSize() default 100;
}

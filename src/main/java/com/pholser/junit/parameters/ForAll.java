package com.pholser.junit.parameters;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import org.junit.experimental.theories.ParametersSuppliedBy;

@Target(PARAMETER)
@Retention(RUNTIME)
@ParametersSuppliedBy(GeneratingParameterSupplier.class)
public @interface ForAll {
    int sampleSize() default 100;
}

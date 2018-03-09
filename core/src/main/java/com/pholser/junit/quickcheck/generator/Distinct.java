package com.pholser.junit.quickcheck.generator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>Mark a parameter of a {@link com.pholser.junit.quickcheck.Property}
 * method with this annotation to make values generated for the parameter
 * distinct from each other.</p>
 *
 * <p>This annotation is recognized on array parameters and parameters of type
 * {@link java.util.Collection} and {@link java.util.Map}.</p>
 *
 * <p>Using this annotation with {@link Size} on {@link java.util.Set} or
 * {@link java.util.Map} leads to strict size constraint.</p>
 */
@Target({ PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE })
@Retention(RUNTIME)
@GeneratorConfiguration
public @interface Distinct {
}

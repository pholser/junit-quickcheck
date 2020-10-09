package com.pholser.junit.quickcheck.internal.generator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>Mark a parameter of a {@link com.pholser.junit.quickcheck.Property}
 * method with this annotation to indicate that the parameter is nullable, and
 * to optionally configure the probability of generating a null value.</p>
 */
@Target({ PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE })
@Retention(RUNTIME)
public @interface NullAllowed {
    /**
     * @return probability of generating null {@code float} value, in the range [0,1]
     */
    float probability() default 0.2f;
}

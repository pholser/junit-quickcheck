package com.pholser.junit.quickcheck.internal.generator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>Mark a parameter of a {@link com.pholser.junit.quickcheck.Property}
 * method with this annotation, in addition to {@link javax.annotation.Nullable},
 * to configure the probability of generating a null value.</p>
 */
@Target({ PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE })
@Retention(RUNTIME)
public @interface NullAllowed {
    /**
     * @return probability of generating null {@code float} value, in the range [0,1]
     */
    float probability() default 0.2f;
}

/*
 The MIT License

 Copyright (c) 2010-2016 Paul R. Holser, Jr.

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.pholser.junit.quickcheck;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.pholser.junit.quickcheck.internal.RandomValueSupplier;
import org.junit.contrib.theories.ParametersSuppliedBy;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Mark a parameter of a {@link org.junit.contrib.theories.Theory} method
 * with this annotation to have random values supplied to it.
 *
 * @deprecated Rather than writing property-based tests as {@code Theory}
 * methods with parameters marked with this annotation, write them as
 * {@link Property} methods on classes run with the
 * {@link com.pholser.junit.quickcheck.runner.JUnitQuickcheck} runner.
 */
@Deprecated
@Target(PARAMETER)
@Retention(RUNTIME)
@ParametersSuppliedBy(RandomValueSupplier.class)
public @interface ForAll {
    /**
     * @return the number of generated values to give the annotated parameter
     */
    int sampleSize() default 100;

    /**
     * @return the ratio of discarded generated values to successful generated
     * values above which values will no longer be generated. For a negative
     * value, generation will cease immediately. For a zero value, generation
     * will stop if the number of discarded generated values exceeds the
     * {@linkplain #sampleSize() sample size}.
     */
    int discardRatio() default 0;

    /**
     * @return an <a href="http://commons.apache.org/ognl/">OGNL</a> expression
     * used to constrain the values fed to the annotated theory parameter.
     * The expression should evaluate to a {@code boolean} value. Within the
     * expression, {@code "_"} refers to the theory parameter.
     */
    String suchThat() default "";

    /**
     * @return a value to be used as the initial seed for the random value
     * generator. Not specifying a value causes the seed to be chosen in the
     * usual JDK way.
     */
    long seed() default 0;
}

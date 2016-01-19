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

package com.pholser.junit.quickcheck.generator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * <p>Mark a parameter of a {@link com.pholser.junit.quickcheck.Property}
 * method with this annotation to constrain the values generated for the
 * parameter to a given range.</p>
 *
 * <p>Different generators may use different min/max attribute pairs.
 * Generators that produce primitive values or values of their wrapper types
 * will likely want to use the attribute pairs of corresponding type.
 * Otherwise, a generator can use {@link #min()} and {@link #max()}, and
 * take on the responsibility of converting their string values to values of
 * the desired type.</p>
 */
@Target({ PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE })
@Retention(RUNTIME)
@GeneratorConfiguration
public @interface InRange {
    /**
     * @return a minimum {@code byte} value
     */
    byte minByte() default Byte.MIN_VALUE;

    /**
     * @return a maximum {@code byte} value
     */
    byte maxByte() default Byte.MAX_VALUE;

    /**
     * @return a minimum {@code short} value
     */
    short minShort() default Short.MIN_VALUE;

    /**
     * @return a maximum {@code short} value
     */
    short maxShort() default Short.MAX_VALUE;

    /**
     * @return a minimum {@code char} value
     */
    char minChar() default Character.MIN_VALUE;

    /**
     * @return a maximum {@code char} value
     */
    char maxChar() default Character.MAX_VALUE;

    /**
     * @return a minimum {@code int} value
     */
    int minInt() default Integer.MIN_VALUE;

    /**
     * @return a maximum {@code int} value
     */
    int maxInt() default Integer.MAX_VALUE;

    /**
     * @return a minimum {@code long} value
     */
    long minLong() default Long.MIN_VALUE;

    /**
     * @return a maximum {@code long} value
     */
    long maxLong() default Long.MAX_VALUE;

    /**
     * @return a minimum {@code float} value
     */
    float minFloat() default 0F;

    /**
     * @return a maximum {@code float} value
     */
    float maxFloat() default 1F;

    /**
     * @return a minimum {@code double} value
     */
    double minDouble() default 0D;

    /**
     * @return a maximum {@code double} value
     */
    double maxDouble() default 1D;

    /**
     * @return a minimum value, represented in string form
     */
    String min() default "";

    /**
     * @return a maximum value, represented in string form
     */
    String max() default "";

    /**
     * @return a formatting hint, such as a
     * {@linkplain java.text.SimpleDateFormat date format string}, that
     * generators can use when converting values from strings
     */
    String format() default "";
}

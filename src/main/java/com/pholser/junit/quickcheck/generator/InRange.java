/*
 The MIT License

 Copyright (c) 2010-2012 Paul R. Holser, Jr.

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

import com.pholser.junit.quickcheck.ForAll;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Mark a parameter of a {@link org.junit.contrib.theories.Theory Theory} method already marked with
 * {@link ForAll} with this annotation to constrain the values generated for the parameter to a given range.
 */
@Target(PARAMETER)
@Retention(RUNTIME)
@GeneratorConfiguration
public @interface InRange {
    byte minByte() default Byte.MIN_VALUE;

    byte maxByte() default Byte.MAX_VALUE;

    short minShort() default Short.MIN_VALUE;

    short maxShort() default Short.MAX_VALUE;

    char minChar() default Character.MIN_VALUE;

    char maxChar() default Character.MAX_VALUE;

    int minInt() default Integer.MIN_VALUE;

    int maxInt() default Integer.MAX_VALUE;

    long minLong() default Long.MIN_VALUE;

    long maxLong() default Long.MAX_VALUE;

    float minFloat() default -Float.MAX_VALUE;

    float maxFloat() default Float.MAX_VALUE;

    double minDouble() default -Double.MAX_VALUE;

    double maxDouble() default Double.MAX_VALUE;

    String min() default "";

    String max() default "";

    String format() default "";
}

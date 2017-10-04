/*
 The MIT License

 Copyright (c) 2010-2017 Paul R. Holser, Jr.

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

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.conversion.StringConversion;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * <p>Mark a parameter of a {@link Property} method with this annotation to
 * limit the set of values of that parameter used to test the property.</p>
 *
 * <p>In {@linkplain com.pholser.junit.quickcheck.Mode#SAMPLING sampling}
 * mode, on each trial junit-quickcheck chooses a value from the limited set
 * at random with equal probability.
 *
 * <p>In {@linkplain com.pholser.junit.quickcheck.Mode#EXHAUSTIVE exhaustive}
 * mode, junit-quickcheck will use the values in the limited set for the
 * property parameter, and no others.</p>
 *
 * <p><strong>Note</strong>: When a parameter is marked with this annotation,
 * junit-quickcheck doesn't call upon any generators. Thus, it ignores any
 * generator configuration annotations present on the parameter.</p>
 */
@Target({ PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE })
@Retention(RUNTIME)
public @interface Only {
    /**
     * @return the values to which the property parameter should be limited
     */
    String[] value();

    /**
     * <p>How to convert the values in {@link #value()} into instances of the
     * property parameter's type.</p>
     *
     * <p>If not specified, junit-quickcheck employs the following strategies
     * for value conversion for the marked parameter:</p>
     * <ul>
     *   <li>If present, use a {@code public static} method on the property
     *   parameter type called {@code valueOf} which accepts a single
     *   {@code String} argument and whose return type is the type itself.</li>
     *   <li>Otherwise, if present, use a {@code public} constructor on the
     *   property parameter type which accepts a single {@code String}
     *   argument.</li>
     * </ul>
     *
     * @return a type for a value conversion strategy
     */
    Class<? extends StringConversion> by() default StringConversion.class;
}

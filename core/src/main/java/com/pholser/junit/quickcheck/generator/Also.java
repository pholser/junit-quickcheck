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
 * ensure that a limited set of values are guaranteed to be used as the value
 * of that parameter to test the property.</p>
 *
 * <p>In {@linkplain com.pholser.junit.quickcheck.Mode#SAMPLING sampling}
 * mode, on each trial junit-quickcheck ensures that the first <em>n</em>
 * values are the given set, and generates the remaining <em>trials - n</em>
 * values in the usual way.
 *
 * <p>In {@linkplain com.pholser.junit.quickcheck.Mode#EXHAUSTIVE exhaustive}
 * mode, junit-quickcheck uses the values in the limited set for the property
 * parameter, and generates the remaining <em>trials - n</em> values in the
 * usual way.</p>
 *
 * <p><strong>Note</strong>: You will still need a generator defined for the
 * property parameter's type when this annotation is used.</p>
 */
@Target({ PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE })
@Retention(RUNTIME)
public @interface Also {
    /**
     * @return the values to which the property parameter will certainly be
     * assigned during property verification
     */
    String[] value();

    /**
     * @see Only#by()
     */
    Class<? extends StringConversion> by() default StringConversion.class;
}

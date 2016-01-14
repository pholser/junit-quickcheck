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

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import com.pholser.junit.quickcheck.generator.Generator;

/**
 * <p>Mark a parameter of a {@link Property} method with this annotation to
 * have random values supplied to it via the specified
 * {@link Generator}.</p>
 *
 * <p>You may specify as many of these annotations as as you wish on a given
 * parameter. On a given generation, one of the specified generators will be
 * chosen at random with probability in proportion to {@link #frequency()}.</p>
 *
 * <p>If any such generator produces values of a type incompatible with the
 * type of the marked parameter, {@link IllegalArgumentException} is
 * raised.</p>
 */
@Target({ PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(Produced.class)
public @interface From {
    /**
     * @return the generator to be used for the annotated property parameter
     */
    Class<? extends Generator> value();

    /**
     * @return a weight to influence how often the generator is chosen
     */
    int frequency() default 1;
}

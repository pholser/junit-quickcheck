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

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import com.pholser.junit.quickcheck.hook.NilMinimalCounterexampleHook;

/**
 * <p>Mark a method on a class that is {@linkplain org.junit.runner.RunWith
 * run with} the {@link com.pholser.junit.quickcheck.runner.JUnitQuickcheck}
 * runner with this annotation to have it run as a property-based test.</p>
 *
 * <p>A method marked with this annotation should be an instance method
 * declared as {@code public} with a return type of {@code void}.</p>
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface Property {
    /**
     * @return how many sets of parameters to verify the property with
     */
    int trials() default 100;

    /**
     * @return whether or not to attempt to {@linkplain
     * com.pholser.junit.quickcheck.generator.Shrink shrink} a failing set
     * of parameters
     */
    boolean shrink() default true;

    /**
     * @return the maximum number of {@linkplain
     * com.pholser.junit.quickcheck.generator.Shrink shrink} attempts to make
     * on a failing set of parameters; in effect only when {@link #shrink()}
     * is {@code true}
     */
    int maxShrinks() default 100;

    /**
     * @return the maximum depth of {@linkplain
     * com.pholser.junit.quickcheck.generator.Shrink shrink} tree to make on
     * a failing set of parameters; in effect only when {@link #shrink()} is
     * {@code true}
     */
    int maxShrinkDepth() default 20;

    /**
     * @return the maximum elapsed time for the shrinking process in
     * milliseconds; in effect only when {@link #shrink()} is {@code true}
     */
    int maxShrinkTime() default 60_000;

    /**
     * @return callback that it is executed if a minimal counterexample is found
     * (after shrinking)
     */
    Class<? extends MinimalCounterexampleHook> onMinimalCounterexample()
        default NilMinimalCounterexampleHook.class;
}

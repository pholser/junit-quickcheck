
package com.pholser.junit.quickcheck;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.pholser.junit.quickcheck.hook.NilMinimalCounterexampleHook;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import static com.pholser.junit.quickcheck.Mode.*;

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
     * @return the verification mode for the property
     */
    Mode mode() default SAMPLING;

    /**
     * @return how many sets of parameters to verify the property with, in
     * {@link Mode#SAMPLING} mode; in {@link Mode#EXHAUSTIVE} mode, how many
     * values to generate for each property parameter
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

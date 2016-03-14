package com.pholser.junit.quickcheck;

/**
 * Allows the user to get hold of an actual failing example (useful if the object's
 * {@link Object#toString()} representation is difficult to understand).
 *
 * @see Property#onMinimalCounterexample()}
 */
public interface MinimalCounterexampleHook {
    /**
     * @param counterexample the minimal counterexample (after shrinking)
     * @param action work to perform with the minimal counterexample; for example,
     * this could repeat the test using the same inputs. This action should
     * be safely callable multiple times.
     */
    void handle(Object[] counterexample, Runnable action);
}

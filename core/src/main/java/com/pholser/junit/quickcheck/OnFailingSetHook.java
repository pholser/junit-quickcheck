package com.pholser.junit.quickcheck;

/**
 * Allows the user to get hold of an actual failing example (useful if the object's
 * toString representation is difficult to understand).
 *
 * To install the hook, overwrite {@link Property#onFailingSet()} for a failing test.
 */
public interface OnFailingSetHook {

    /**
     * @param counterExample the minimal counter example (after shrinking)
     */
    void handle(Object[] counterExample);

}

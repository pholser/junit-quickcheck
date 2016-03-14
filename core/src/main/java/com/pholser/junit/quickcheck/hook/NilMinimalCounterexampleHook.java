package com.pholser.junit.quickcheck.hook;

import com.pholser.junit.quickcheck.MinimalCounterexampleHook;

public class NilMinimalCounterexampleHook implements MinimalCounterexampleHook {
    @Override
    public void handle(Object[] counterexample, Runnable action) {
        // do nothing purposely
    }
}

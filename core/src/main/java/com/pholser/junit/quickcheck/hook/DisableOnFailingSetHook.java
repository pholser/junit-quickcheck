package com.pholser.junit.quickcheck.hook;

import com.pholser.junit.quickcheck.OnFailingSetHook;

public class DisableOnFailingSetHook implements OnFailingSetHook {

    @Override
    public void handle(Object[] counterExample, Runnable repeatTestOption) {}

}

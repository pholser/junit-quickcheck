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

package com.pholser.junit.quickcheck.internal;

import com.pholser.junit.quickcheck.MinimalCounterexampleHook;
import com.pholser.junit.quickcheck.Property;

public class ShrinkControl {
    private final boolean shouldShrink;
    private final int maxShrinks;
    private final int maxShrinkDepth;
    private final int maxShrinkTime;
    private final MinimalCounterexampleHook onMinimalCounterexample;

    public ShrinkControl(Property marker) throws IllegalAccessException, InstantiationException {
        this.shouldShrink = marker.shrink();
        this.maxShrinks = marker.maxShrinks();
        this.maxShrinkDepth = marker.maxShrinkDepth();
        this.maxShrinkTime = marker.maxShrinkTime();
        this.onMinimalCounterexample = marker.onMinimalCounterexample().newInstance();
    }

    public boolean shouldShrink() {
        return shouldShrink;
    }

    public int maxShrinks() {
        return maxShrinks;
    }

    public int maxShrinkDepth() {
        return maxShrinkDepth;
    }

    public int maxShrinkTime() {
        return maxShrinkTime;
    }

    public MinimalCounterexampleHook onMinimalCounterexample() {
        return onMinimalCounterexample;
    }
}

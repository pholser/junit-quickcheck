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

package com.pholser.junit.quickcheck.internal.generator;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnumGeneratorTest {
    private EnumGenerator generator;

    @Before public void setUp() {
        generator = new EnumGenerator(ElementType.class);
    }

    @Test public void capabilityOfShrinkingNonEnum() {
        assertFalse(generator.canShrink(new Object()));
    }

    @Test public void capabilityOfShrinkingEnumOfDesiredType() {
        assertTrue(generator.canShrink(ElementType.METHOD));
    }

    @Test public void capabilityOfShrinkingEnumOfOtherType() {
        assertFalse(generator.canShrink(RetentionPolicy.SOURCE));
    }
}

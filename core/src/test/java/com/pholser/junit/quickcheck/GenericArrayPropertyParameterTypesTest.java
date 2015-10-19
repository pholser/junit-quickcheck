/*
 The MIT License

 Copyright (c) 2010-2015 Paul R. Holser, Jr.

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

import com.pholser.junit.quickcheck.internal.Zilch;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.Box;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertThat;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

public class GenericArrayPropertyParameterTypesTest {
    @Test public void arrayOfBoxOfHuh() {
        assertThat(testResult(ArrayOfBoxOfHuh.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ArrayOfBoxOfHuh {
        @Property(trials = 5) public void shouldHold(Box<?>[] items) {
        }
    }

    @Test public void arrayOfBoxOfZilch() {
        assertThat(testResult(ArrayOfBoxOfZilch.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ArrayOfBoxOfZilch {
        @Property(trials = 5) public void shouldHold(Box<Zilch>[] items) {
        }
    }

    @Test public void arrayOfBoxOfSuperZilch() {
        assertThat(testResult(ArrayOfBoxOfSuperZilch.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ArrayOfBoxOfSuperZilch {
        @Property(trials = 5) public void shouldHold(Box<? super Zilch>[] items) {
        }
    }
}

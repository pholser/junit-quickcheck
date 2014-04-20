/*
 The MIT License

 Copyright (c) 2010-2013 Paul R. Holser, Jr.

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

import com.pholser.junit.quickcheck.generator.java.lang.Encoded;
import org.junit.Test;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import java.nio.charset.Charset;

import static com.pholser.junit.quickcheck.generator.java.lang.Encoded.InCharset;
import static java.nio.charset.Charset.defaultCharset;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ForAllStringTheoryParameterTypesTest {
    @Test public void inDefaultCharset() {
        assertThat(testResult(DefaultCharset.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class DefaultCharset {
        @Theory public void shouldHold(@ForAll @From(Encoded.class) String s) {
            assertTrue(defaultCharset().newEncoder().canEncode(s));
        }
    }

    @Test public void inSomeOther() {
        assertThat(testResult(SomeOtherCharset.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class SomeOtherCharset {
        @Theory public void shouldHold(@ForAll @From(Encoded.class) @InCharset("UTF-8") String s) {
            assertTrue(Charset.forName("UTF-8").newEncoder().canEncode(s));
        }
    }
}

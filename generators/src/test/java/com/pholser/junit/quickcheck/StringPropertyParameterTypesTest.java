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

import java.nio.charset.Charset;

import com.pholser.junit.quickcheck.generator.java.lang.Encoded;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.generator.java.lang.Encoded.*;
import static java.nio.charset.Charset.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class StringPropertyParameterTypesTest {
    @Test public void fromAnyStringSource() {
        assertThat(testResult(AnyStringSource.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class AnyStringSource {
        @Property public void shouldHold(String s) {
        }
    }

    @Test public void inDefaultCharset() {
        assertThat(testResult(DefaultCharset.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class DefaultCharset {
        @Property public void shouldHold(@From(Encoded.class) String s) {
            assertTrue(defaultCharset().newEncoder().canEncode(s));
        }
    }

    @Test public void inSomeOtherCharset() {
        assertThat(testResult(SomeOtherCharset.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class SomeOtherCharset {
        @Property public void shouldHold(@From(Encoded.class) @InCharset("UTF-8") String s) {
            assertTrue(Charset.forName("UTF-8").newEncoder().canEncode(s));
        }
    }

    @Test public void shrinkingFromAnyStringSource() {
        assertThat(testResult(ShrinkingAnyStringSource.class), failureCountIs(1));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingAnyStringSource {
        static String failed;

        @Property public void shouldHold(String s) {
            assumeThat(s.length(), greaterThan(5));

            failed = s;

            fail();
        }
    }

    @Test public void shrinkingInDefaultCharset() {
        assertThat(testResult(ShrinkingDefaultCharset.class), failureCountIs(1));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingDefaultCharset {
        static String failed;

        @Property public void shouldHold(@From(Encoded.class) String s) {
            assumeThat(s.length(), greaterThan(5));
            assertTrue(defaultCharset().newEncoder().canEncode(s));

            failed = s;

            fail();
        }
    }

    @Test public void shrinkingInSomeOtherCharset() {
        assertThat(testResult(ShrinkingSomeOtherCharset.class), failureCountIs(1));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingSomeOtherCharset {
        static String failed;

        @Property public void shouldHold(@From(Encoded.class) @InCharset("ISO-8859-1") String s) {
            assumeThat(s.length(), greaterThan(5));
            assertTrue(Charset.forName("ISO-8859-1").newEncoder().canEncode(s));

            failed = s;

            fail();
        }
    }
}

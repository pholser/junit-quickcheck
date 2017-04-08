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

package com.pholser.junit.quickcheck;

import java.util.Properties;
import java.util.function.IntPredicate;

import com.pholser.junit.quickcheck.generator.java.lang.Encoded.InCharset;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.lang.Character.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class PropertiesPropertyParameterTypesTest {
    @Test public void unmarked() {
        assertThat(testResult(Unmarked.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Unmarked {
        @Property public void shouldHold(Properties p) {
            p.forEach((key, value) -> {
                assertThat(key, instanceOf(String.class));
                assertThat(value, instanceOf(String.class));
                IntPredicate inCharset = ch -> ch >= 0 && ch < MIN_SURROGATE;
                assertMatches((String) key, inCharset);
                assertMatches((String) value, inCharset);
            });
        }
    }

    @Test public void marked() {
        assertThat(testResult(Marked.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Marked {
        @Property public void shouldHold(@InCharset("US-ASCII") Properties p) {
            p.forEach((key, value) -> {
                assertThat(key, instanceOf(String.class));
                assertThat(value, instanceOf(String.class));
                IntPredicate inCharset = ch -> ch >= 0 && ch < 0x80;
                assertMatches((String) key, inCharset);
                assertMatches((String) value, inCharset);
            });
        }
    }

    private static void assertMatches(String s, IntPredicate p) {
        assertTrue(s.chars().allMatch(p));
    }
}

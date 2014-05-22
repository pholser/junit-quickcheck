/*
 The MIT License

 Copyright (c) 2010-2014 Paul R. Holser, Jr.

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

import java.util.Formatter;

import com.pholser.junit.quickcheck.generator.ValuesOf;
import com.pholser.junit.quickcheck.test.generator.Between;
import org.junit.Test;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class MarkedWithSuperfluousConfigurationTest {
    @Test public void okIfParameterMarkedWithUnexpectedConfiguration() {
        assertThat(testResult(WithSuperfluousConfiguration.class), isSuccessful());
        assertEquals(Formatter.BigDecimalLayoutForm.values().length, WithSuperfluousConfiguration.iterations);
    }

    @RunWith(Theories.class)
    public static class WithSuperfluousConfiguration {
        static int iterations;

        @Theory public void shouldHold(@ForAll @ValuesOf @Between(min = 3, max = 6) Formatter.BigDecimalLayoutForm f) {
            ++iterations;
        }
    }
}

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

import com.pholser.junit.quickcheck.test.generator.Box;
import com.pholser.junit.quickcheck.test.generator.Foo;
import org.junit.Test;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Annotations.*;
import static com.pholser.junit.quickcheck.test.generator.FooGenerator.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class CompositeGeneratorsPassConfigurationOnToComponentsTest {
    @Test public void boxOfFoo() throws Exception {
        assertThat(testResult(BoxOfFoo.class), isSuccessful());
        assertEquals(defaultSampleSize(), BoxOfFoo.iterations);
    }

    @RunWith(Theories.class)
    public static class BoxOfFoo {
        static int iterations;

        @Theory public void holds(@ForAll @Same(5) Box<Foo> b) {
            ++iterations;

            assertEquals(5, b.contents().getI());
        }
    }

    @Test public void arrayOfFoo() throws Exception {
        assertThat(testResult(ArrayOfFoo.class), isSuccessful());
        assertEquals(defaultSampleSize(), ArrayOfFoo.iterations);
    }

    @RunWith(Theories.class)
    public static class ArrayOfFoo {
        static int iterations;

        @Theory public void holds(@ForAll @Same(6) Foo[] f) {
            ++iterations;

            for (Foo each : f)
                assertEquals(6, each.getI());
        }
    }
}

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

import com.pholser.junit.quickcheck.generator.Ctor;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ContractTestWithTypeVariableForPropertyParameterTest {
    public static class TestObject {
        private final String str;

        public TestObject( String str ) {
            this.str = str;
        }

        @Override public String toString() {
            return str;
        }
    }

    public static class G extends Generator<TestObject> {
        public G() {
            super(TestObject.class);
        }

        @Override public TestObject generate(SourceOfRandomness r, GenerationStatus s) {
            return new TestObject("yermom");
        }
    }

    public interface ContractTest<T> {
        @Property default void testProperty(@From(Ctor.class) T value) {
            assertEquals(value, value);
        }
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ObjectTest implements ContractTest<TestObject> {
    }

    @Test public void gitHubIssue143() {
        assertThat(testResult(ObjectTest.class), isSuccessful());
    }
}

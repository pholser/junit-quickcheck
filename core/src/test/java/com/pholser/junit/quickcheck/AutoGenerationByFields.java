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

import com.pholser.junit.quickcheck.generator.Fields;
import com.pholser.junit.quickcheck.internal.Zilch;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.test.generator.Box;
import com.pholser.junit.quickcheck.test.generator.Foo;
import com.pholser.junit.quickcheck.test.generator.TestArrayListGenerator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;
import static org.junit.rules.ExpectedException.*;

public class AutoGenerationByFields {
    @Rule public final ExpectedException thrown = none();

    @Test public void autoGeneration() {
        assertThat(testResult(WithAutoGeneration.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WithAutoGeneration {
        public static class P {
            public Zilch z;
            public Foo f;
            public Box<Foo> b;
        }

        @Theory public void shouldHold(@ForAll @From(Fields.class) P p) {
        }
    }

    @Test public void autoGenerationOnGenericType() {
        assertThat(testResult(WithAutoGenerationOnGenericType.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WithAutoGenerationOnGenericType {
        @Theory public void shouldHold(@ForAll @From(Fields.class) FakeList<Foo> list) {
        }
    }

    public static class FakeList<T> {
    }

    @Test public void autoGeneratorDoesNotAllowItselfToBeRegistered() {
        GeneratorRepository repo = new GeneratorRepository(null);

        repo.register(new Fields<Object>(Object.class));

        thrown.expect(IllegalArgumentException.class);
        repo.generatorFor(Object.class);
    }
}

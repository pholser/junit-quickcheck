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

import com.pholser.junit.quickcheck.generator.Fields;
import com.pholser.junit.quickcheck.internal.ReflectionException;
import com.pholser.junit.quickcheck.internal.Zilch;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.test.generator.Between;
import com.pholser.junit.quickcheck.test.generator.Box;
import com.pholser.junit.quickcheck.test.generator.Foo;
import com.pholser.junit.quickcheck.test.generator.FooGenerator.Same;
import com.pholser.junit.quickcheck.test.generator.Mark;
import com.pholser.junit.quickcheck.test.generator.TestIntegerGenerator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.experimental.results.PrintableResult;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;
import static org.junit.rules.ExpectedException.*;

public class AutoGenerationByFieldsTest {
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

        repo.register(new Fields<>(Object.class));

        thrown.expect(IllegalArgumentException.class);
        repo.generatorFor(Object.class);
    }

    @Test public void autoGenerationOnPrimitiveType() {
        PrintableResult result = testResult(WithAutoGenerationOnPrimitiveType.class);
        assertThat(result, hasSingleFailureContaining(ReflectionException.class.getName()));
        assertThat(result, hasSingleFailureContaining(InstantiationException.class.getName()));
    }

    @RunWith(Theories.class)
    public static class WithAutoGenerationOnPrimitiveType {
        @Theory public void shouldHold(@ForAll @From(Fields.class) int i) {
        }
    }

    @Test public void autoGenerationOnPrimitiveWrapperType() {
        PrintableResult result = testResult(WithAutoGenerationOnPrimitiveWrapperType.class);
        assertThat(result, hasSingleFailureContaining(ReflectionException.class.getName()));
        assertThat(result, hasSingleFailureContaining(InstantiationException.class.getName()));
    }

    @RunWith(Theories.class)
    public static class WithAutoGenerationOnPrimitiveWrapperType {
        @Theory public void shouldHold(@ForAll @From(Fields.class) Float f) {
        }
    }

    @Test public void autoGenerationWithAnnotations() {
        assertThat(testResult(WithAutoGenerationWithAnnotations.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WithAutoGenerationWithAnnotations {
        public static class P {
            @From(TestIntegerGenerator.class) @Between(min = 2, max = 4) public int i;
        }

        @Theory public void shouldHold(@ForAll @From(Fields.class) P p) {
            assertThat(p.i, allOf(greaterThanOrEqualTo(2), lessThanOrEqualTo(4)));
        }
    }

    @Test public void autoGenerationWithAggregateAnnotation() {
        assertThat(testResult(WithAutoGenerationWithAggregateAnnotations.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WithAutoGenerationWithAggregateAnnotations {
        @Target({PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE})
        @Retention(RUNTIME)
        @From(TestIntegerGenerator.class)
        @Between(min = 1, max = 5)
        public @interface Small {
        }

        public static class P {
            @Small public int i;
        }

        @Theory public void shouldHold(@ForAll @From(Fields.class) P p) {
            assertThat(p.i, allOf(greaterThanOrEqualTo(1), lessThanOrEqualTo(5)));
        }
    }

    @Test public void autoGenerationWithAnnotationsOnTypeUsesInFields() {
        assertThat(testResult(WithAutoGenerationWithAnnotationsOnTypeUsesInFields.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WithAutoGenerationWithAnnotationsOnTypeUsesInFields {
        public static class P {
            public Box<@Mark Foo> box;
        }

        @Theory public void shouldHold(@ForAll @From(Fields.class) P p) {
            assertFalse(p.box.marked());
            assertTrue(p.box.contents().marked());
        }
    }

    @Test public void autoGenerationWithAggregateAnnotationsOnTypeUsesInFields() {
        assertThat(testResult(WithAutoGenerationWithAggregateAnnotationsOnTypeUsesInFields.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class WithAutoGenerationWithAggregateAnnotationsOnTypeUsesInFields {
        @Target({PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE})
        @Retention(RUNTIME)
        @Mark
        @Same(2)
        public @interface MarkTwo {
        }

        public static class P {
            public Box<@MarkTwo Foo> box;
        }

        @Theory public void shouldHold(@ForAll @From(Fields.class) P p) {
            assertFalse(p.box.marked());
            assertTrue(p.box.contents().marked());
            assertEquals(2, p.box.contents().i());
        }
    }
}

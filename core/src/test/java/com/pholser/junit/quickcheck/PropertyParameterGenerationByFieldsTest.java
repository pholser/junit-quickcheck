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

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.pholser.junit.quickcheck.generator.Fields;
import com.pholser.junit.quickcheck.internal.ReflectionException;
import com.pholser.junit.quickcheck.internal.Zilch;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.AFoo.Same;
import com.pholser.junit.quickcheck.test.generator.AnInt;
import com.pholser.junit.quickcheck.test.generator.Between;
import com.pholser.junit.quickcheck.test.generator.Box;
import com.pholser.junit.quickcheck.test.generator.Foo;
import com.pholser.junit.quickcheck.test.generator.X;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.results.PrintableResult;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Types.*;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;
import static org.junit.rules.ExpectedException.*;

public class PropertyParameterGenerationByFieldsTest {
    @Rule public final ExpectedException thrown = none();

    private static Object object;

    @Test public void autoGeneration() {
        assertThat(testResult(WithAutoGeneration.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WithAutoGeneration {
        public static class P {
            public Zilch z;
            public Foo f;
            public Box<Foo> b;
        }

        @Property public void shouldHold(@From(Fields.class) P p) {
        }
    }

    @Test public void autoGenerationOnGenericType() {
        assertThat(testResult(WithAutoGenerationOnGenericType.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WithAutoGenerationOnGenericType {
        @Property public void shouldHold(@From(Fields.class) FakeList<Foo> list) {
        }
    }

    public static class FakeList<T> {
    }

    @Test public void autoGeneratorDoesNotAllowItselfToBeRegistered() throws Exception {
        GeneratorRepository repo = new GeneratorRepository(null);

        repo.register(new Fields<>(Object.class));

        thrown.expect(IllegalArgumentException.class);
        repo.generatorFor(typeOf(getClass(), "object"));
    }

    @Test public void autoGenerationOnPrimitiveType() {
        PrintableResult result = testResult(WithAutoGenerationOnPrimitiveType.class);
        assertThat(result, hasSingleFailureContaining(ReflectionException.class.getName()));
        assertThat(result, hasSingleFailureContaining(InstantiationException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WithAutoGenerationOnPrimitiveType {
        @Property public void shouldHold(@From(Fields.class) int i) {
        }
    }

    @Test public void autoGenerationOnPrimitiveWrapperType() {
        PrintableResult result = testResult(WithAutoGenerationOnPrimitiveWrapperType.class);
        assertThat(result, hasSingleFailureContaining(ReflectionException.class.getName()));
        assertThat(result, hasSingleFailureContaining(InstantiationException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WithAutoGenerationOnPrimitiveWrapperType {
        @Property public void shouldHold(@From(Fields.class) Float f) {
        }
    }

    @Test public void autoGenerationWithAnnotations() {
        assertThat(testResult(WithAutoGenerationWithAnnotations.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WithAutoGenerationWithAnnotations {
        public static class P {
            @From(AnInt.class) @Between(min = 2, max = 4) public int i;
        }

        @Property public void shouldHold(@From(Fields.class) P p) {
            assertThat(p.i, allOf(greaterThanOrEqualTo(2), lessThanOrEqualTo(4)));
        }
    }

    @Test public void autoGenerationWithAggregateAnnotation() {
        assertThat(testResult(WithAutoGenerationWithAggregateAnnotations.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WithAutoGenerationWithAggregateAnnotations {
        @Target({PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE})
        @Retention(RUNTIME)
        @From(AnInt.class)
        @Between(min = 1, max = 5)
        public @interface Small {
        }

        public static class P {
            @Small public int i;
        }

        @Property public void shouldHold(@From(Fields.class) P p) {
            assertThat(p.i, allOf(greaterThanOrEqualTo(1), lessThanOrEqualTo(5)));
        }
    }

    @Test public void autoGenerationWithAnnotationsOnTypeUsesInFields() {
        assertThat(testResult(WithAutoGenerationWithAnnotationsOnTypeUsesInFields.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WithAutoGenerationWithAnnotationsOnTypeUsesInFields {
        public static class P {
            public Box<@X Foo> box;
        }

        @Property public void shouldHold(@From(Fields.class) P p) {
            assertFalse(p.box.marked());
            assertTrue(p.box.contents().marked());
        }
    }

    @Test public void autoGenerationWithAggregateAnnotationsOnTypeUsesInFields() {
        assertThat(testResult(WithAutoGenerationWithAggregateAnnotationsOnTypeUsesInFields.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WithAutoGenerationWithAggregateAnnotationsOnTypeUsesInFields {
        @Target({PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE})
        @Retention(RUNTIME)
        @X
        @Same(2)
        public @interface MarkTwo {
        }

        public static class P {
            public Box<@MarkTwo Foo> box;
        }

        @Property public void shouldHold(@From(Fields.class) P p) {
            assertFalse(p.box.marked());
            assertTrue(p.box.contents().marked());
            assertEquals(2, p.box.contents().i());
        }
    }

    public static class Tuple3<A, B, C> {
        public A first;
        public B second;
        public C third;
    }

    @Test public void autoGenerationWithUnresolvedTypeVariablesInFields() {
        assertThat(
            testResult(WithAutoGenerationWithUnresolvedTypeVariablesInFields.class),
            hasSingleFailureContaining("No variable substitution established"));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WithAutoGenerationWithUnresolvedTypeVariablesInFields<A, B, C> {
        @Property public void shouldHold(@From(Fields.class) Tuple3<A, B, C> t) {
        }
    }
}

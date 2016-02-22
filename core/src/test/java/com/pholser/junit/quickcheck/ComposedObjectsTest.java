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

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.ABox;
import com.pholser.junit.quickcheck.test.generator.AFoo.Same;
import com.pholser.junit.quickcheck.test.generator.AnotherBox;
import com.pholser.junit.quickcheck.test.generator.Box;
import com.pholser.junit.quickcheck.test.generator.Foo;
import com.pholser.junit.quickcheck.test.generator.X;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ComposedObjectsTest {
    @Test public void askingForGeneratorsByType() {
        assertThat(testResult(AskingForGeneratorsByType.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class AskingForGeneratorsByType {
        static class A {
            Foo foo;
        }

        public static class MakeA extends Generator<A> {
            public MakeA() {
                super(A.class);
            }

            @Override public A generate(SourceOfRandomness random, GenerationStatus status) {
                A a = new A();
                a.foo = gen().type(Foo.class).generate(random, status);
                return a;
            }
        }

        @Property public void holds(@From(MakeA.class) A a) {
        }
    }

    @Test public void askingForGeneratorsForIndividualFields() {
        assertThat(testResult(AskingForIndividualFields.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class AskingForIndividualFields {
        static class A {
            @X Foo foo;
            @X Box<@Same(3) Foo> boxOfFoo;
        }

        public static class MakeA extends Generator<A> {
            public MakeA() {
                super(A.class);
            }

            @SuppressWarnings("unchecked")
            @Override public A generate(SourceOfRandomness random, GenerationStatus status) {
                A a = new A();
                a.foo = (Foo) gen().field(A.class, "foo").generate(random, status);
                a.boxOfFoo = (Box<Foo>) gen().field(A.class, "boxOfFoo").generate(random, status);
                return a;
            }
        }

        @Property public void holds(@From(MakeA.class) A a) {
            assertTrue(a.foo.marked());
            assertTrue(a.boxOfFoo.marked());
            assertFalse(a.boxOfFoo.contents().marked());
            assertEquals(3, a.boxOfFoo.contents().i());
        }
    }

    @Test public void askingForGeneratorsForAllFieldsOfClassAtOnce() {
        assertThat(testResult(AskingForAllFieldsAtOnce.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class AskingForAllFieldsAtOnce {
        public static class B {
            Foo foo;
            @From(ABox.class) Box<@X @From(AnotherBox.class) Box<@X Foo>> boxOfBoxOfFoo;
        }

        public static class A {
            B b;
        }

        public static class MakeA extends Generator<A> {
            public MakeA() {
                super(A.class);
            }

            @Override public A generate(SourceOfRandomness random, GenerationStatus status) {
                A a = new A();
                a.b = gen().fieldsOf(B.class).generate(random, status);
                return a;
            }
        }

        @Property public void holds(@From(MakeA.class) A a) {
            assertFalse(a.b.foo.marked());
            assertFalse(a.b.boxOfBoxOfFoo.marked());
            assertTrue(a.b.boxOfBoxOfFoo.contents().marked());
            assertTrue(a.b.boxOfBoxOfFoo.contents().contents().marked());
        }
    }

    @Test public void askingForGeneratorsByConstructor() {
        assertThat(testResult(AskingByConstructor.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class AskingByConstructor {
        public static class B {
            final Foo foo;
            final Box<@X Box<@X Foo>> boxOfBoxOfFoo;

            public B(@Same(6) Foo foo, Box<@X Box<@X @Same(7) Foo>> boxOfBoxOfFoo) {
                this.foo = foo;
                this.boxOfBoxOfFoo = boxOfBoxOfFoo;
            }
        }

        public static class A {
            B b;
        }

        public static class MakeA extends Generator<A> {
            public MakeA() {
                super(A.class);
            }

            @Override public A generate(SourceOfRandomness random, GenerationStatus status) {
                A a = new A();
                a.b = gen().constructor(B.class, Foo.class, Box.class).generate(random, status);
                return a;
            }
        }

        @Property public void holds(@From(MakeA.class) A a) {
            assertFalse(a.b.foo.marked());
            assertFalse(a.b.boxOfBoxOfFoo.marked());
            assertTrue(a.b.boxOfBoxOfFoo.contents().marked());
            assertTrue(a.b.boxOfBoxOfFoo.contents().contents().marked());
        }
    }

    @Test public void askingForGeneratorsByUnrecognizedConstructor() {
        assertThat(
            testResult(AskingByUnrecognizedConstructor.class),
            hasSingleFailureContaining("java.lang.IllegalArgumentException: No constructor found for class"));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class AskingByUnrecognizedConstructor {
        public static class B {
            final Foo foo;
            final Box<@X Box<@X Foo>> boxOfBoxOfFoo;

            public B(@Same(6) Foo foo, Box<@X Box<@X @Same(7) Foo>> boxOfBoxOfFoo) {
                this.foo = foo;
                this.boxOfBoxOfFoo = boxOfBoxOfFoo;
            }
        }

        public static class A {
            B b;
        }

        public static class MakeA extends Generator<A> {
            public MakeA() {
                super(A.class);
            }

            @Override public A generate(SourceOfRandomness random, GenerationStatus status) {
                A a = new A();
                a.b = gen().constructor(B.class, int.class).generate(random, status);
                return a;
            }
        }

        @Property public void holds(@From(MakeA.class) A a) {
        }
    }

    @Test
    public void askingForArrayGenerator() {
        assertThat(testResult(AskingForArrayGenerator.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class AskingForArrayGenerator {
        static class A {
            Foo[][] foos;
        }

        public static class MakeA extends Generator<A> {
            public MakeA() {
                super(A.class);
            }

            @Override public A generate(SourceOfRandomness random, GenerationStatus status) {
                A a = new A();
                a.foos = gen().type(Foo[][].class).generate(random, status);
                return a;
            }
        }

        @Property public void holds(@From(MakeA.class) A a) {
        }
    }

    @Test
    public void askingForRawComponentizedType() {
        assertThat(testResult(AskingForRawComponentizedType.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class AskingForRawComponentizedType {
        static class A {
            Box b;
        }

        public static class MakeA extends Generator<A> {
            public MakeA() {
                super(A.class);
            }

            @Override public A generate(SourceOfRandomness random, GenerationStatus status) {
                A a = new A();
                a.b = gen().type(Box.class).generate(random, status);
                return a;
            }
        }

        @Property public void holds(@From(MakeA.class) A a) {
        }
    }

    @Test
    public void askingForArrayOfRawComponentizedType() {
        assertThat(testResult(AskingForArrayOfRawComponentizedType.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class AskingForArrayOfRawComponentizedType {
        static class A {
            Box[] b;
        }

        public static class MakeA extends Generator<A> {
            public MakeA() {
                super(A.class);
            }

            @Override public A generate(SourceOfRandomness random, GenerationStatus status) {
                A a = new A();
                a.b = gen().type(Box[].class).generate(random, status);
                return a;
            }
        }

        @Property public void holds(@From(MakeA.class) A a) {
        }
    }
}
